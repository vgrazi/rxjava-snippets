package com.vgrazi.play;

import rx.Observable;
import rx.Subscription;
import rx.observables.ConnectableObservable;

import java.util.concurrent.TimeUnit;

/**
 * Created by victorg on 7/28/2016.
 */
public class ConnectedPublisherExamples {

    public static void main(String[] args) throws Exception{
//        Observable<Long> hotObservable = Observable.interval(1, TimeUnit.SECONDS);
        ConnectableObservable<Long> hotObservable = Observable.interval(1, TimeUnit.SECONDS).publish();

//        hotObservable.refCount();
        hotObservable.autoConnect(2);
//        hotObservable.autoConnect(2, s->System.out.println(s + " connected"));
        Subscription subscriber1 = hotObservable.subscribe(val -> System.out.println("Subscriber 1 >> " + val));

        Thread.sleep(4000);

        Subscription subscriber2 = hotObservable.subscribe(val -> System.out.println("Subscriber 2 >> " + val));
//        Thread.sleep(1000);
//        subscriber1.unsubscribe();

//        Thread.sleep(1000);
//        subscriber2.unsubscribe();
//
//        hotObservable.connect();

        Thread.sleep(50_000);
    }

}
