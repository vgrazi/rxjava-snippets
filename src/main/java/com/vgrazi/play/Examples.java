package com.vgrazi.play;

import java.util.Date;

public class Examples {
  public static void main(String[] args) {
//    SomeFeed<PriceTick> feed = new SomeFeed<>();
//
//    Observable<PriceTick> observable =
//      Observable.fromAsync((AsyncEmitter<PriceTick> emitter) -> {
//        SomeListener listener = new SomeListener() {
//          @Override
//          public void priceTick(PriceTick event) {
//            emitter.onNext(event);
//            if (event.isLast()) {
//              emitter.onCompleted();
//            }
//          }
//
//          @Override
//          public void error(Throwable e) {
//            emitter.onError(e);
//          }
//        };
//
//        feed.register(listener);
//      }, AsyncEmitter.BackpressureMode.BUFFER);
//
//
//    observable.subscribe(x -> System.out.printf("%2d %s %4s %6.2f%n", x.getSequence(), x.getDate(), x.getInstrument(), x.getPrice()), System.out::println, () -> System.out.println("Complete"));
//    Observable<Integer> range = Observable.range(1, Integer.MAX_VALUE);
//    Observable<String> words = Observable.just(
//      "the",
//      "quick",
//      "brown",
//      "fox",
//      "jumped",
//      "over",
//      "the",
//      "lazy",
//      "dog"
//    );
//    words.
//      flatMap(w -> Observable.from(w.split("")))
//      .distinct()
//      .sorted()
//      .zipWith(Observable.range(1, Integer.MAX_VALUE), (letter, counter) -> String.format("%2d. %s", counter, letter))
//      .subscribe(System.out::println);
//
//    Observable<Long> weekday = Observable.interval(5, TimeUnit.SECONDS)
//      .filter(x -> isWeekend());
//
//    Observable<Long> weekend = Observable.interval(2, TimeUnit.SECONDS)
//      .filter(x -> !isWeekend());
//
//    Observable.merge(weekend, weekday)
//      .subscribe(x -> System.out.println(new Date()));
//
//    sleep(100_000);
  }


  private static boolean isWeekend() {
    Date now = new Date();
    return now.getSeconds() > 30;
  }
}
