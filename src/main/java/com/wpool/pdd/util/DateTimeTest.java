package com.wpool.pdd.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.TimeUnit;

/**
 *
 */
public class DateTimeTest {

    public static void main(String[] args) {
        LocalDateTime now = LocalDateTime.now();
        System.out.println(now);
        //LocalDateTime tenSecondsLater = now.plusSeconds(10);
        try {
            TimeUnit.SECONDS.sleep(10);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime tenSecondsLater = LocalDateTime.now();
        System.out.println(tenSecondsLater);

        long diff = ChronoUnit.SECONDS.between(now, tenSecondsLater);

        System.out.println(diff);


    }
}
