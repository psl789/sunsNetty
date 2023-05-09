package com.suns;

public class TestCompacity {
    public static void main(String[] args) {
        int newCapacity = 65;
        newCapacity |= newCapacity >>>  1;
        newCapacity |= newCapacity >>>  2;
        newCapacity |= newCapacity >>>  4;
        newCapacity |= newCapacity >>>  8;
        newCapacity |= newCapacity >>> 16;
        newCapacity ++;

        System.out.println("newCapacity = " + newCapacity);
    }
}
