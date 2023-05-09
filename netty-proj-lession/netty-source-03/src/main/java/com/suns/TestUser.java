package com.suns;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TestUser {
    public static void main(String[] args) throws NoSuchFieldException, IllegalAccessException {

        for (; ; ) {
            System.out.println("hello sunshuai");
        }

        /*
        User user = new User();
        Class<? extends User> userClazz = user.getClass();


        Field name = userClazz.getDeclaredField("name");
        log.debug("name {} ", name);

        //打破封装
        name.setAccessible(true);

        //user.name
        name.set(user, "xiaohei");
        log.debug("value is {} ",name.get(user));
        */
    }
}
