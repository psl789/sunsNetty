package com.suns.netty.handler;

import com.suns.netty.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicBoolean;

@Slf4j
public class ClientLogicHandler extends ChannelInboundHandlerAdapter {
    Scanner scanner = new Scanner(System.in);
    CountDownLatch WAIT_LOGIN;
    AtomicBoolean LOGIN;
    AtomicBoolean SERVER_ERROR;
    Bootstrap bootstrap;
    private int tryCountMax =3;
    private int currentCount=0;
    public ClientLogicHandler(AtomicBoolean login, CountDownLatch wait_login, AtomicBoolean server_error, Bootstrap bootstrap) {
        this.WAIT_LOGIN = wait_login;
        this.LOGIN = login;
        this.SERVER_ERROR = server_error;
        this.bootstrap = bootstrap;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        log.debug("recive data {}", msg);
        if (msg instanceof LoginResponseMessage) {
            LoginResponseMessage loginResponseMessage = (LoginResponseMessage) msg;
            if (loginResponseMessage.getCode().equals("200")) {
                LOGIN.set(true);
            }
            WAIT_LOGIN.countDown();
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        if (SERVER_ERROR.get()) {
                               /*
                               当前执行这行代码的线程 ，后续会被netty回收，所以在这里直接重连不好
                                Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
                                channel.closeFuture().sync();
                                */
            log.debug("重连...");

            //等价于从EventLoopGroup中 从新获得了
            EventLoop eventLoop = ctx.channel().eventLoop();
            reconnect(eventLoop);

        } else {
            log.debug("client close ...");
        }

    }

    private void reconnect(EventLoop eventLoop) {
        eventLoop.submit(() -> {
            Channel channel = null;
            ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
            connect.addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture future) throws Exception {
                    if(future.isSuccess()){
                        log.info("重连成功");
                    }else {
                        if(currentCount>=tryCountMax){
                            log.info("重连失败");
                            return;
                        }
                        currentCount++;
                        reconnect(eventLoop);
                    }
                }
            });
            try {
                channel.closeFuture().sync();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        log.debug("client close....");
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //用户名 密码 让用户输入 Scanner 输入
        //用户的输入 菜单的展示  UI相关的内容
        new Thread(() -> {
            System.out.println("请输入用户名: ");
            String username = scanner.nextLine();

            System.out.println("请输入密码: ");
            String password = scanner.nextLine();

            //发送登录操作
            LoginRequestMessage loginRequestMessage = new LoginRequestMessage(username, password);
            ctx.writeAndFlush(loginRequestMessage);

            try {
                WAIT_LOGIN.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            if (!LOGIN.get()) {
                ctx.channel().close();
                return;
            }

            while (true) {
                System.out.println("========================================");
                System.out.println("send [username] [content]");
                System.out.println("gcreate [group name] [m1,m2,m3...]");
                System.out.println("gsend [group name] [content]");
                System.out.println("quit");
                System.out.println("========================================");

                String command = scanner.nextLine();
                String[] args = command.split(" ");
                switch (args[0]) {
                    case "send":
                        ctx.writeAndFlush(new ChatRequestMessage(username, args[1], args[2]));
                        break;
                    case "gcreate":
                        String groupName = args[1];
                        String[] membersString = args[2].split(",");
                        Set<String> members = new HashSet<>(Arrays.asList(membersString));
                        members.add(username);
                        ctx.writeAndFlush(new GroupCreateRequestMessage(groupName, members));
                        break;
                    case "gsend":
                        String gName = args[1];
                        String content = args[2];
                        ctx.writeAndFlush(new GroupChatRequestMessage(username, gName, content));
                        break;
                    case "quit":
                        //服务器端 unbind
                        ctx.channel().close();
                        return;
                }
            }
        }, "Client UI").start();

    }
}
