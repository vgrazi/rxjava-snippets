package com.vgrazi.play;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class SomeFeed {
  private final boolean barriered;
  private AtomicInteger threadcounter = new AtomicInteger(1);

  private ExecutorService service = Executors.newCachedThreadPool(r -> {
    Thread thread = new Thread(r);
    thread.setName("Thread " + threadcounter.getAndIncrement());
    return thread;
  });
  private transient boolean running = true;

  private List<SomeListener> listeners = new LinkedList<>();
  private int threadCount;
  private CyclicBarrier barrier;

  private final Random RANDOM = new Random(0);
  private static final Random RANDOM_PRICE = new Random(0);

  public SomeFeed() {
    this(1);
  }

  public SomeFeed(int threadCount) {
    this(threadCount, false);
  }

  public SomeFeed(int threadCount, boolean barriered) {
    this.threadCount = threadCount;
    this.barriered = barriered;
    if (barriered) {
      barrier = new CyclicBarrier(threadCount, System.out::println);
    }
    launchPublishers();
  }


  private void launchEventThread(String instrument, double startingPrice) {
    service.execute(() ->
    {
      int counter = 0;
      final Object MUTEX = new Object();
      double price = startingPrice;
      while (running) {
        try {
          if (barriered) {
            barrier.await();
          }
          price +=  RANDOM_PRICE.nextGaussian();

          double finalPrice = price;
          listeners.forEach(subscriber -> {
            PriceTick priceTick = new PriceTick(new Date(), instrument, finalPrice);
            subscriber.priceTick(priceTick);
          });
          synchronized (MUTEX) {
            MUTEX.wait(RANDOM.nextInt(500) + 500);
          }
        } catch (InterruptedException | BrokenBarrierException e) {
          e.printStackTrace();
        }
      }
    });
  }

  String[] instruments = {"IBM", "NMR", "BAC", "AAPL", "MSFT"};
  double[] prices = {160, 5, 15,  108, 57};
  void launchPublishers() {
    for (int i = 0; i < threadCount; i++) {
      launchEventThread(instruments[i%instruments.length], prices[i%prices.length]);
    }
  }

  void register(SomeListener listener) {
    listeners.add(listener);
  }

  public void terminate() {
    running = false;
  }

}
