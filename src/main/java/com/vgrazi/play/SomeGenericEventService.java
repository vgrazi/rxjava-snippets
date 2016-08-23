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

public class SomeGenericEventService {
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

    public SomeGenericEventService(int threadCount, boolean barriered)
    {
        this.threadCount = threadCount;
        this.barriered = barriered;
        barrier = new CyclicBarrier(threadCount, System.out::println);
    }

    private void launchEventThread() {
        service.execute(() ->
        {
            int counter = 0;
            final Object MUTEX = new Object();
            SimpleDateFormat format = new SimpleDateFormat("hh:mm:ss.SSS");
            while (running) {
                try {
                    if (barriered) {
                        barrier.await();
                    }
                    listeners.forEach(subscriber -> subscriber.priceTick(String.format("%s %s", Thread.currentThread().getName(), format.format(new Date()))));
                    synchronized (MUTEX) {
                        MUTEX.wait(RANDOM.nextInt(500) + 500);
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    void launchPublishers() {
        for (int i = 0; i < threadCount; i++) {
            launchEventThread();
        }
    }

    void register(SomeListener listener) {
        listeners.add(listener);
    }

    public void terminate() {
        running = false;
    }

}
