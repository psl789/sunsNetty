package com.suns.test;

public class TestByteBuf {
    public static void main(String[] args) {
        //ByteBuf buf = ByteBufAllocator.DEFAULT.buffer();

        /*int i = 10;
        i<<=1;
        System.out.println(i);*/

        for (int i = 512; i > 0; i <<= 1) {
            System.out.println(i);
        }
        //2147483647
        System.out.println("int 最大值"+Integer.MAX_VALUE);
    }
}

