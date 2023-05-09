package com.suns;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

import java.util.concurrent.TimeUnit;

public class TestEventloop2 {
    public static void main(String[] args) {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        eventLoopGroup.setIoRatio(80);

        EventLoop eventLoop = eventLoopGroup.next();


        eventLoop.execute(() -> {
            System.out.println("hello suns");
        });

        eventLoop.schedule(() -> {
        }, 5, TimeUnit.SECONDS);
    }
}
