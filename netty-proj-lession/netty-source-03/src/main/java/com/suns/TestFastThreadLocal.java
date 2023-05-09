package com.suns;

import io.netty.util.concurrent.FastThreadLocal;

public class TestFastThreadLocal {
    public static final FastThreadLocal<String> f1 = new FastThreadLocal<>();
    public static final FastThreadLocal<String> f2 = new FastThreadLocal<>();

    public static void main(String[] args) {
     /*   FastThreadLocalThread t1 = new FastThreadLocalThread(() -> {
            f1.set("sunshuai");// InternalThreadLocalMap object 1
            f2.set("xiaohei");// InternalThreadLocalMap object 2

            Thread thread = Thread.currentThread();

            System.out.println("TestFastThreadLocal.main");

        }, "t1");

        t1.start();
*/

        FastThreadLocal f3 = new FastThreadLocal(){
           /* @Override
            protected Object initialValue() throws Exception {
                return "is value";
            }*/
        };

        Object o = f3.get();
        System.out.println("o = " + o);
    }
}
