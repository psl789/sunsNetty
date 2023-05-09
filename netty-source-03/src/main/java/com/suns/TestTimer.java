package com.suns;


import lombok.extern.slf4j.Slf4j;

import java.util.Timer;
import java.util.TimerTask;

/*
  TimerTask  ---> 类似 Runnable
 */
@Slf4j
public class TestTimer extends TimerTask {
    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new TestTimer(), 5000);
    }

    @Override
    public void run() {
        log.debug("Timer Task Run");
    }
}
