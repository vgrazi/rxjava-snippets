package com.vgrazi.play;

import org.junit.BeforeClass;
import org.junit.Test;
import rx.Observable;
import rx.Subscriber;

public class MultiThreadedObservable {

    private final Object MUTEX = new Object();
    private static SomeGenericEventService eventService = new SomeGenericEventService(2, false);

    @BeforeClass
    public static void launchPublishers() {
        eventService.launchPublishers();
    }

    @Test
    public void test() throws InterruptedException {

        final int[] count = {0};
        Observable<String> observable = Observable.create((subscriber) -> eventService.register(
                new SomeListener() {

                    @Override
                    public void priceTick(String event) {
                        if (count[0]++ == 5) {
                            System.out.println("Completing " + subscriber);
                            subscriber.onCompleted();
                            System.out.println("Completed " + subscriber);
                        } else {
                            subscriber.onNext(event);
                        }
                    }

                  @Override
                  public void error(Throwable throwable) {
                    subscriber.onError(throwable);
                  }
                }));
        observable
//                .buffer(2, TimeUnit.SECONDS)
//                .count()
//                .toBlocking()

                .subscribe(new Subscriber<String>() {
                    @Override
                    public void onCompleted() {
                        synchronized (MUTEX) {
                            MUTEX.notify();
                        }
                    }

                    @Override
                    public void onError(Throwable throwable) {

                    }

                    @Override
                    public void onNext(String s) {
                        System.out.println(s);
                    }
                });

//        // add a delay
        synchronized (MUTEX) {
            MUTEX.wait(10000L);
            eventService.terminate();
        }
    }

}
