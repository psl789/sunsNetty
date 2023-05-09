package com.suns;

import io.netty.channel.EventLoop;
import io.netty.channel.nio.NioEventLoopGroup;

import java.nio.channels.Selector;

public class TestEventLoop {
    public static void main(String[] args) throws Exception {
        EventLoop eventLoop = new NioEventLoopGroup().next();

        Selector open = Selector.open();
        System.out.println("TestEventLoop.main");
    }
}
