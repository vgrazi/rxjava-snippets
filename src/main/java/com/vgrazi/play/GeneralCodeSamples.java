package com.vgrazi.play;

import com.vgrazi.util.Logger;
import org.junit.Test;
import rx.*;
import rx.Observable;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.observables.ConnectableObservable;
import rx.observables.MathObservable;
import rx.schedulers.Schedulers;
import rx.subjects.AsyncSubject;
import rx.subjects.Subject;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.*;

import static com.vgrazi.util.Logger.print;

public class GeneralCodeSamples {

  @Test
  public void testCreate() {
    Observable.create(s -> s.onNext("Hello, world"))
      .subscribe(System.out::println);

  }

  @Test
  public void testCreateTwo() {
    Observable.create(
      subscriber -> {
        subscriber.onNext("Hello,");
        subscriber.onNext("World");
      }
    )
      .subscribe(System.out::println);

  }

  @Test
  public void testCreateWithComplete() {
    Observable.create(
      subscriber -> {
        subscriber.onNext("Hello,");
        subscriber.onNext("World");
        subscriber.onCompleted();
      }
    )
      .subscribe(System.out::println, System.out::println, () -> System.out.println("Complete"));

  }

  @Test
  public void testCreateWithError() {
    Observable<Object> observable = Observable.create(
      subscriber -> {
        subscriber.onNext("Hello,");
        subscriber.onNext("World");
        subscriber.onError(new IOException("test exception"));
        subscriber.onNext("Ignored after error");
        subscriber.onCompleted();
      }
    );

    observable.subscribe(System.out::println, System.out::println, () -> System.out.println("Complete"));
  }

  @Test
  public void testCreateWithErrorCorrection() {

    Observable<String> observable = Observable.create(
      subscriber -> {
        subscriber.onNext("Hello,");
        subscriber.onNext("World");
        subscriber.onError(new IOException("test exception"));
        subscriber.onNext("Ignored after error");
//        subscriber.onError(new IOException("test exception"));
        subscriber.onNext("Ignored after 2nd error");
        subscriber.onCompleted();
      }
    );

//    Observable<String> observable1 = observable
//      .onErrorReturn(o -> "ERROR CORRECTED")
//      .doOnError(System.out::println);

    ConcurrentLinkedDeque<Observable<?>> sources = new ConcurrentLinkedDeque<>();
    Observable<String> observable1 = Observable.combineLatestDelayError(sources, args -> Arrays.asList(args).toString());
    observable1.subscribe(System.out::println,
      System.out::println, () -> System.out.println("Complete"));
    System.out.println(sources);
    Scheduler scheduler = Schedulers.immediate();
//    Scheduler scheduler = Schedulers.computation();
//    Scheduler scheduler = Schedulers.from(Executor);
//    Scheduler scheduler = Schedulers.io();
//    Scheduler scheduler = Schedulers.newThread();
//    Scheduler scheduler = Schedulers.trampoline();
    observable.subscribeOn(scheduler);
  }

  @Test
  public void testConvertObservableToInt() {
    Integer sum = MathObservable.sumInteger(Observable.just(1, 2, 8, 9, 10, 11)).toBlocking().first();
    System.out.println(sum);

    List<String> list = Observable.just("it", "was", "the", "best", "of", "times").buffer(100).toBlocking().single();
    System.out.println(list);
  }

  @Test
  public void testRangeZip() {
    Observable<Integer> counters = Observable
      .range(1, Integer.MAX_VALUE)
//      .doOnNext(x-> System.out.println("On Next:" + x))
      ;
    List<String> list = Arrays.asList(
      "the",
      "quick",
      "brown",
      "fox",
      "jumped",
      "over",
      "the",
      "lazy",
      "dog"
    );
    Observable
      .from(list)
      .flatMap(x-> Observable.from(x.split("")))
      .distinct()
      .sorted()
      .zipWith(counters, (x, y)->String.format("%2d. %s", y, x))
      .subscribe(System.out::println);
  }


  @Test
  public void testFromAsync() {
    SomeFeed feed = new SomeFeed();

//    Observable<PriceTick> observable = Observable.fromAsync((AsyncEmitter<PriceTick> emitter) -> {
//      SomeListener listener = new SomeListener() {
//        @Override
//        public void priceTick(PriceTick event) {
////          emitter.onNext(throwException());
//          emitter.onNext(event);
//          if (event.isLast()) {
//            emitter.onCompleted();
//          }
//        }
//
//        @Override
//        public void error(Throwable e) {
//          emitter.onError(e);
//        }
//      };
//
//
//      feed.register(listener);
//
//    }, AsyncEmitter.BackpressureMode.BUFFER).doOnNext((x) -> print("doOnNext:" + x));
////    ConnectableObservable<PriceTick> connectableObservable = observable.publish();
////    connectableObservable.connect();
//    sleep(3_000);
//
////    Subscription subscription = observable.subscribe(Logger::print);
//    Subscription subscription = observable.subscribe(Logger::print, Logger::print, ()-> System.out.println("Complete"));
//    sleep(3_000);
//    subscription.unsubscribe();

  }


  @Test
  public void testAttachFeed() {
    SomeFeed feed = new SomeFeed(3);
    Observable.create(s ->
      feed.register(new SomeListener() {
        @Override
        public void priceTick(PriceTick event) {
          s.onNext(event);
        }

        @Override
        public void error(Throwable throwable) {
          s.onError(throwable);
        }
      })
    ).subscribe(System.out::println);

    sleep(500_000);
  }

//  @Test
//  public void testAttachFeedWithMap() {
//    SomeFeed feed = new SomeFeed(3);
//    Observable.create(s ->
//      feed.register(new SomeListener() {
//        @Override
//        public void priceTick(String event) {
//          s.onNext(event);
//        }
//
//        @Override
//        public void error(Throwable throwable) {
//          s.onError(throwable);
//        }
//      })
//    )
//      .subscribe(System.out::println);
//
//    sleep(500_000);
//  }


  @Test
  public void just_1() {
    Observable.just("Hello, World")
      .subscribe(System.out::println);
  }

  @Test
  public void just_2() {
    Observable.just("Hello", "World")
      .subscribe(System.out::println);
  }

  @Test
  public void buffer() {
    Observable.just("Hello", "World")
      .buffer(2)
      .subscribe(System.out::println);
  }

  @Test
  public void bufferWithOverflow() {
    Observable.just("Hello", "World")
      .buffer(30)
      .subscribe(System.out::println);
  }

  @Test
  public void testJustWithNull() {
    Observable.just("Hello", "World", null)
      .buffer(30)
      .subscribe(System.out::println);
  }

  @Test
  public void testJustWithNullFiltered() {
    Observable.just("Hello", "World", null)
      .filter(s -> s != null)
      .buffer(30)
      .subscribe(System.out::println);
  }

  @Test
  public void testJustWithNullsFiltered() {
    List<String> list = Arrays.asList("Hello", null, "World", null, null, "Goodbye", "Cruel", "World");
    Observable.from(list)
      .filter(s -> s != null)
      .buffer(2)
      .subscribe(System.out::println);
  }

  @Test
  public void testMax() {
    Integer[] list = new Integer[]{10, 20, null, 30, 40, 50};
    Observable<Integer> observable = Observable.from(list);
    Observable<Integer> buffer = MathObservable.from(observable)
      .max(Integer::compareTo);
    buffer.subscribe(System.out::println,
      Throwable::printStackTrace,
      () -> System.out.println("Complete"));
  }

  @Test
  public void testSum() {
    Integer[] list = new Integer[]{10, 20, null, 30, 40, 50};
    Observable<Integer> observable = Observable.from(list)
      .filter(s -> s != null);
    Observable<Integer> buffer = MathObservable.from(observable)
      .sumInteger(s -> s * 10);

    buffer.subscribe(System.out::println,
      Throwable::printStackTrace,
      () -> System.out.println("Complete"));
  }

  @Test
  public void testMath() {
    Integer[] list = new Integer[]{10, 60, null, 65, 70, 80};
    Observable<Integer> observable = Observable.from(list)
      .filter(s -> s != null);

    Observable<Double> sum = MathObservable.from(observable)
      .sumDouble(s -> s * 1d);

    Observable<Double> max = MathObservable.from(observable)
      .max(Integer::compareTo)
      .map(s -> s * 1.);

    Observable<Double> min = MathObservable.from(observable)
      .min(Integer::compareTo)
      .map(s -> s * 1.);

    Observable<Double> medianStream = max.mergeWith(min);

    Observable<Double> midrange = MathObservable.from(medianStream).averageDouble(s -> s);
    midrange.subscribe((x) -> System.out.println("Mid range:" + x));

    Observable<Integer> midrangeInt = MathObservable.from(medianStream).averageInteger(Double::intValue);
    midrangeInt.subscribe((x) -> System.out.println("Mid range int:" + x));

    sum.subscribe((x) -> System.out.println("Sum:" + x),
      Throwable::printStackTrace,
      () -> System.out.println("Complete"));
  }

//  @Test
//  public void testReduce() {
//    Integer[] list = new Integer[]{10, 60, null, 65, 70, 80};
//    Observable<Integer> observable = Observable.from(list)
//      .filter(s -> s != null);
//
//    Observable<Double> sum = MathObservable.from(observable)
//      .sumDouble(s -> s * 1d);
//    ;
//
//    Observable<Double> max = MathObservable.from(observable)
//      .max(Integer::compareTo)
//      .map(s -> s * 1.);
//
//    Observable<Double> min = MathObservable.from(observable)
//      .min(Integer::compareTo)
//      .map(s -> s * 1.);
//
//    Observable<Double> medianStream = max.mergeWith(min);
//
//    Observable<Double> midrange = MathObservable.from(medianStream).averageDouble(s -> s);
//    midrange.subscribe((x) -> System.out.println("Mid range:" + x));
//
//    Observable<Integer> midrangeInt = MathObservable.from(medianStream).averageInteger(Double::intValue);
//    midrangeInt.subscribe((x) -> System.out.println("Mid range int:" + x));
//
//        Observable<Double> range = MathObservable.from(medianStream).sumDouble(
//                new Func1<Double, Double>() {
//                    @Override
//                    public Double call(Double aDouble) {
//                        return null;
//                    }
//                }
//        );
//        range.reduce(0, (a, b) -> {
//            return abs(b - a);
//        });
//        range.subscribe((x) -> System.out.println("Range int:" + x));
//
//
//    sum.subscribe((x) -> System.out.println("Sum:" + x),
//      Throwable::printStackTrace,
//      () -> System.out.println("Complete"));
//  }

  @Test
  public void test_9() {
    Integer[] list = new Integer[]{10, 20, 30, 40, 50};
//        List<Integer> list = Arrays.asList(10, 20, 30, 40, 50);
    Observable<List<Integer>> buffer = Observable.from(list)
//        Observable<Integer> buffer = Observable.from(list)
      .filter(s -> s != null)
      .buffer(30);
    buffer.subscribe(System.out::println,
      System.out::println,
      () -> System.out.println("Complete"));
  }

  @Test
  public void testRange() {
    Observable<Integer> observable = Observable.range(100, 10);

    observable.subscribe(System.out::println);
  }


  @Test
  public void projectEulerTest1() {
    // sum numbers from 1 to 1000 divisible by 3 and/or 5
    Observable<Integer> valid = Observable.range(1, 999)
      .filter(s -> s % 3 == 0 || s % 5 == 0)
      .doOnNext(System.out::println)
      .reduce(0, (a, b) -> a + b);

    valid.subscribe(System.out::println);
  }

  @Test
  public void projectEulerTest2() {
    Observable<Integer> threes = Observable.range(1, 999).map(i -> i * 3).takeWhile(i -> i < 1000);
    Observable<Integer> fives = Observable.range(1, 999).map(i -> i * 5).takeWhile(i -> i < 1000);
    Observable<Integer> threesAndFives = Observable
      .merge(threes, fives)
      .distinct();

    threesAndFives.reduce(0, (a, b) -> a + b)
      .subscribe(System.out::println);
  }

  @Test
  public void testMidRange() {
    Integer[] list = {10, 80, 90, 100};
    Observable<Integer> ints = Observable.from(list);
    Observable<Integer> min = MathObservable.from(ints).min(Integer::compareTo);
    Observable<Integer> max = MathObservable.from(ints).max(Integer::compareTo);
//    min.zipWith(max, (a, b) -> (b + a) / 2).subscribe(System.out::println);
    Observable.zip(min, max, (a, b) -> (b + a) / 2).subscribe(System.out::println);
  }

  @Test
  public void testSubject() throws InterruptedException {
    ExecutorService executor = Executors.newCachedThreadPool();
    Subject<Object, Object> subject = AsyncSubject.create();
    getThread(executor, subject);
    getThread(executor, subject);

    subject.subscribe(
      v -> System.out.println(v),             // happy path
      System.out::println,                    // error handler
      () -> System.out.println("Complete"));  // complete

    Thread.sleep(12_000);
  }

  private void getThread(ExecutorService executor, Subject<Object, Object> subject) {
    executor.execute(() ->
    {
      for (int i = 0; i < 10; i++) {
        System.out.println("onNext " + i);
        subject.onNext(i);
        try {
          Thread.sleep(500);
        } catch (InterruptedException e) {
          subject.onError(e);
        }
      }
      System.out.println("completing");
      subject.onCompleted();
    });
  }

//    @Test void testGenerateList()
//    {
//        Integer[] ints = {1,2,3,4,5};
//        Observable<Integer> observableList = Observable.from(ints);
//        List list = observableList.extend()
//    }

  @Test
  public void testMergeWith() {
    Observable<String> first = Observable.just("aaaa", "bbbb", "ccc", "dddd");
    Observable<String> second = Observable.just("11111", "2222", "3333", "4444", "555555");
    for (int i = 0; i < 10; i++) {
      Observable<String> combined = first.mergeWith(second);
      combined.subscribe(System.out::println);
      System.out.println();
    }
  }

  @Test
  public void testFlatMap() {
    Observable<String> observable = Observable.just("aaaa", "bbbb", "ccc", "dddd");
    Observable<String> flatMap = observable.flatMap(a -> Observable.just("flat mapped:" + a));
    Observable<String> map = observable.map(a -> "mapped:" + a);

    Action1<String> action = (x) -> System.out.println(x.getClass() + ":" + x + ":" + x.hashCode());
    observable.subscribe(action);

    System.out.println();
    flatMap.subscribe(action);

    System.out.println();
    map.subscribe(action);
  }

  @Test
  public void testSingle() {
    // merge a & b into an Observable stream of 2 values
    Single<String> firstString = Single.create(o -> o.onSuccess("DataA"));
    Single<String> secondString = Single.just("DataB");
    Observable<String> merged = firstString.mergeWith(secondString);
    merged.subscribe(System.out::println);
  }

  @Test
  public void testObserver() {
    Observable<String> observable = Observable.just("aaaa", "bbbb", "ccc", "dddd");
    observable.subscribe(new Action1<String>() {
      @Override
      public void call(String x) {
        System.out.println(x);
      }
    }, new Action1<Throwable>() {
      @Override
      public void call(Throwable x1) {
        System.out.println(x1);
      }
    }, new Action0() {
      @Override
      public void call() {
        System.out.println();
      }
    });
  }

  @Test
  public void testSubscripion() {


  }

  @Test
  public void testTimer() {
    Observable
      .timer(5000, TimeUnit.MILLISECONDS)
      .subscribe(System.out::println);
    sleep(5000);
  }

  @Test
  public void testInterval() {
    Observable.interval(1, TimeUnit.SECONDS)
      .subscribe(System.out::println);
    sleep(5000);
  }


  @Test
  public void testColdObservable() throws InterruptedException {
    Observable<Long> observable =
      Observable.interval(1, TimeUnit.SECONDS);

    observable.subscribe(
      val -> System.out.println("Subscriber 1>> " + val));

    sleep(3000);

    observable.subscribe(
      val -> System.out.println("Subscriber 2>>      " + val));

    sleep(5000);
  }


  @Test
  public void testHotObservable() throws InterruptedException {
    Observable<Long> observable =
      Observable.interval(1, TimeUnit.SECONDS).publish();

    observable.subscribe(
      val -> System.out.println("Subscriber 1>> " + val));

    ((ConnectableObservable) observable).connect();

    sleep(3000);
    observable.subscribe(
      val -> System.out.println("Subscriber 2>>      " + val));

    sleep(5000);
  }

  @Test
  public void test_fibonacci() {
    Observable<Integer> fibonacci = Observable.create(
      observer -> {
        int f1 = 0, f2 = 1, f = 1;
        while (!observer.isUnsubscribed()) {
          observer.onNext(f);
          System.out.println(f);
          f = f1 + f2;
          f1 = f2;
          f2 = f;
        }
      }
    );

    Observable<Integer> evenFibs = fibonacci
      .take(10)
//      .takeWhile(s -> s <= 4_000_000)
//      .filter(s -> s % 2 == 0)
      .doOnNext(System.out::println)
      .reduce(0, (a, b) -> a + b);
    evenFibs.subscribe(System.out::println);
  }

  @Test
  public void testFib2() {
    Observable.just(0).repeat();
    final int[] first = {0};
    final int[] second = {1};
    Observable.range(1, 10)
      .scan((a, b) -> {
        int hold = second[0];
        second[0] = second[0] + first[0];
        first[0] = hold;
        return second[0];
      })
      .subscribe(System.out::println);
  }

  @Test
  public void testMergeIdentical()
  {
    ConnectableObservable<Long> hot = Observable.interval(1, TimeUnit.SECONDS).publish();
    hot.connect();
    hot.mergeWith(hot).subscribe((x) -> System.out.println(System.nanoTime() + " " + x));

    sleep(50_000);

  }


  @Test
  public void testConcurrent() {
    Observable<String> a = Observable.create(s -> {
      new Thread(() -> {
        s.onNext("one");
        s.onNext("two");
        s.onNext("three");
        s.onNext("four");
        s.onNext("five");
        s.onNext("six");
        s.onNext("seven");
        s.onNext("eight");
        s.onNext("nine");
        s.onNext("ten");
        s.onNext("");
        s.onCompleted();
      }).start();
    });

    Observable<String> b = Observable.create(s -> {
      new Thread(() -> {
        s.onNext("HELLO");
        s.onNext("THERE");
        s.onNext("");
        s.onCompleted();
      }).start();
    });

// this subscribes to a and b concurrently, and merges into a third sequential stream
    Observable<String> c = Observable.merge(a, b);

    c.subscribe(System.out::println);
    sleep(3000);
  }


  @Test
  public void testCompletable() {
    Completable.create((Completable.CompletableOnSubscribe) s -> System.out.println(s));
  }

  private static void sleep(long value) {
    try {
      Thread.sleep(value);
    } catch (InterruptedException e) {
      Thread.currentThread().interrupt();
    }
  }
}
