package com.suns;

public class TestThreadLocal {
    public static final ThreadLocal<String> tl = new ThreadLocal<>();

    public static void main(String[] args) {
        new Thread(() -> {
            tl.set("xiaohei");// ThreadLocal获得当前线程 （t1）,t1线程threadLocals这个map的成员变量中存储xiaohei
            Thread t1 = Thread.currentThread();
            System.out.println("TestThreadLocal.main");
        }, "t1").start();

        new Thread(() -> {
            Thread t2 = Thread.currentThread();
            System.out.println("TestThreadLocal.main");
        }, "t2").start();



    }
}
