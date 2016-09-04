package com.vgrazi.play;

import com.vgrazi.util.Logger;
import org.junit.Test;
import rx.*;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.observables.MathObservable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.Subject;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.vgrazi.util.Logger.print;

public class Examples {
  public static void main(String[] args) {
    SomeFeed<PriceTick> feed = new SomeFeed<>();

    Observable<PriceTick> observable =
      Observable.fromAsync((AsyncEmitter<PriceTick> emitter) -> {
        SomeListener listener = new SomeListener() {
          @Override
          public void priceTick(PriceTick event) {
            emitter.onNext(event);
            if (event.isLast()) {
              emitter.onCompleted();
            }}

          @Override
          public void error(Throwable e){ emitter.onError(e); }
        };

        feed.register(listener);
      }, AsyncEmitter.BackpressureMode.BUFFER);



      observable.subscribe(x-> System.out.printf("%2d %s %4s %6.2f%n", x.getSequence(), x.getDate(), x.getInstrument(), x.getPrice()), System.out::println, ()-> System.out.println("Complete"));
  }
}
