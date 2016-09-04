package com.vgrazi.util;

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
}
