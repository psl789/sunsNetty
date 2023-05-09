package com.suns;

import io.netty.util.HashedWheelTimer;
import io.netty.util.Timeout;
import io.netty.util.TimerTask;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestHashWheelTimer {
    public static void main(String[] args) {
        //创建时间轮，创建线程，控制Timer的数量
        HashedWheelTimer timer = new HashedWheelTimer(1, TimeUnit.SECONDS,20);

        //添加延时任务
        timer.newTimeout(new TimerTask() {
            @Override
            public void run(Timeout timeout) throws Exception {
                log.debug("test hashtimers");
            }
        }, 1, TimeUnit.SECONDS);

        //16  32
       /* int i = normalizeTicksPerWheel(20);
        log.debug("i {} ",i);*/
    }

   /* private static int normalizeTicksPerWheel(int ticksPerWheel) {
        int normalizedTicksPerWheel = 1;
        while (normalizedTicksPerWheel < ticksPerWheel) {
            normalizedTicksPerWheel <<= 1;
        }
        return normalizedTicksPerWheel;
    }*/


}
