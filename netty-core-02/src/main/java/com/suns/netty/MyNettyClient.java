package com.suns.netty;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.suns.netty.codec.ChatByteToMessageDecoder;
import com.suns.netty.codec.ChatMessageToByteEncoder;
import com.suns.netty.handler.ClientIdleStateHandler;
import com.suns.netty.handler.ClientLogicHandler;
import com.suns.netty.message.*;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import lombok.extern.slf4j.Slf4j;

import java.net.InetSocketAddress;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;


@Slf4j
public class MyNettyClient {

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {

        CountDownLatch WAIT_LOGIN = new CountDownLatch(1);
        AtomicBoolean LOGIN = new AtomicBoolean(false);
        AtomicBoolean SERVER_ERROR = new AtomicBoolean(false);

        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        LoggingHandler LOGGING_HANDLER = new LoggingHandler();

        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.channel(NioSocketChannel.class);
            bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, 50);
            Bootstrap group = bootstrap.group(eventLoopGroup);
            bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
                @Override
                protected void initChannel(NioSocketChannel ch) throws Exception {
                    ch.pipeline().addLast(new LengthFieldBasedFrameDecoder(1024, 7, 4, 0, 0));
                    ch.pipeline().addLast(LOGGING_HANDLER);
                    ch.pipeline().addLast(new ChatMessageToByteEncoder());
                    ch.pipeline().addLast(new ChatByteToMessageDecoder());
                    ch.pipeline().addLast(new IdleStateHandler(8, 3, 0, TimeUnit.SECONDS));
                    ch.pipeline().addLast(new ClientIdleStateHandler(SERVER_ERROR));
                    ch.pipeline().addLast(new ClientLogicHandler(LOGIN,WAIT_LOGIN,SERVER_ERROR,bootstrap));
                }
            });
            Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
            channel.closeFuture().sync();
         /*   ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
            connect.addListener(promise -> {
                   if(!promise.isSuccess()){
                      //重试 3次
                   }else{
                       Channel channel = connect.channel();
                       channel.closeFuture().sync();
                   }
            });*/


        } catch (InterruptedException e) {
            log.error("client error ", e);
        } finally {
            eventLoopGroup.shutdownGracefully();
        }

    }
}