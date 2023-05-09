package com.suns;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.logging.LoggingHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetSocketAddress;
import java.nio.charset.Charset;

public class MyNettyClient {
    private static final Logger log = LoggerFactory.getLogger(MyNettyClient.class);

    public static void main(String[] args) throws InterruptedException {
        log.debug("myNettyClientStarter------");
        EventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        Bootstrap bootstrap = new Bootstrap();
        bootstrap.channel(NioSocketChannel.class);
        Bootstrap group = bootstrap.group(eventLoopGroup);//32 ---> 1 IO操作 31线程
        bootstrap.handler(new ChannelInitializer<NioSocketChannel>() {
            @Override
            protected void initChannel(NioSocketChannel ch) throws Exception {
                ch.pipeline().addLast(new LoggingHandler());
                ch.pipeline().addLast(new ChannelInboundHandlerAdapter(){
                    @Override
                    public void channelActive(ChannelHandlerContext ctx) throws Exception {
                        if (ctx.channel().isWritable()) {
                            ByteBufAllocator alloc = ctx.alloc();
                            ByteBuf buffer = alloc.buffer();
                            buffer.writeCharSequence("xiaohei", Charset.defaultCharset());
                            ctx.writeAndFlush(buffer);
                        }
                    }
                });
            }
        });

        Channel channel = bootstrap.connect(new InetSocketAddress(8000)).sync().channel();
        channel.closeFuture().sync();

    }
}