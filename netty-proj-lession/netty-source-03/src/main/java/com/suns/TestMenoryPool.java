package com.suns;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;

public class TestMenoryPool {
    public static void main(String[] args) {
        //10 ---> 10B ---> tiny --- 16B
        ByteBuf buffer = ByteBufAllocator.DEFAULT.buffer(10);


        Thread thread = Thread.currentThread();
        System.out.println("TestMenoryPool.main");


    }
}
