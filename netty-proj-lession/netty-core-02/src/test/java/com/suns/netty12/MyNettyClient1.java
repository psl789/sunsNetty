package com.suns.netty12;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.codec.string.StringEncoder;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;

public class MyNettyClient1 {
    private static final Logger log = LoggerFactory.getLogger(MyNettyClient1.class);

    public static void main(String[] args) throws InterruptedException, JsonProcessingException {
        log.debug("myNettyClientStarter------");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        Bootstrap group = bootstrap.group(eventLoopGroup);//32 ---> 1 IO操作 31线程
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler());
                ch.pipeline().addLast(new StringEncoder());
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter() {
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        ctx.channel().writeAndFlush("xiaohei");
                    }
                });
            }
        });

        ChannelFuture connect = bootstrap.connect(new InetSocketAddress(8000));
        connect.sync();
        connect.channel().close();


    }
}