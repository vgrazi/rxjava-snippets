package com.vgrazi.util;

import org.junit.Test;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by vgrazi on 9/2/16.
 */
public class Logger {
  public static void print(Object s) {
    System.out.printf("%s:%s%n", new Date(), s);
  }

  public static void sleep(long time) {
    try {
      Thread.sleep(time);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  private static long start = System.currentTimeMillis() / 1000 % 60;

  public static Boolean isSlowTime()
    {
      boolean b = (Calendar.getInstance().get(Calendar.SECOND) - start) % 20 >= 10;
//      System.out.println(new Date() + String.format("is %sslow time", b?"":"NOT "));
      return b;
    }
    @Test
    public void test()
    {
      while(true) {
        System.out.println(String.format(new Date() + " is %sslow time ", isSlowTime()?"":"NOT "));
        sleep(1_00);
      }
    }

}
