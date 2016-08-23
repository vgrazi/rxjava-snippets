package com.vgrazi.play;


import org.junit.Test;
import rx.Observable;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by victorg on 7/13/2016.
 */
@SuppressWarnings("UnnecessaryLocalVariable")
public class reactive {
    public static void main(String[] args) {
        Observable.just(8, 9, 10)
                .doOnNext(i -> System.out.println("A:" + i))
                .filter(i -> i % 3 > 0)
                .doOnNext(i -> System.out.println("B:" + i))
                .map(i -> "#" + i * 10)
                .doOnNext(i -> System.out.println("C:" + i))
                .filter(s -> s.length() < 4)
                .doOnNext(i -> System.out.println("D-:" + i))
                .subscribe(s -> System.out.println("D:" + s))
        ;

    }

    @Test
    public void test() {
        Observable.just(2, 4, 6)
                .doOnNext(System.out::println)
                .subscribe();

    }

    private List<String> wordList = Arrays.asList("Adam", "Dog", "cat", null, "this", "is", "a", "test");

    @Test
    public void test1() {
        Observable.from(wordList)
                .filter(s -> s.length() > 2)
                .map(s -> s + ": " + s.length())
                .subscribe(s -> System.out.println(s));
    }


    private Observable<String> query(String param) {
        Observable<String> query = Observable.from(wordList);
        return query;
    }

    @Test
    public void testWordFlatMap() {

        query("Hello world")
                .flatMap(Observable::just)
                .subscribe(System.out::println);
    }

//    @Test
//    public void testDelay()
//    {
//        Observable.range(1,10)
//                .delay(, TimeUnit.SECONDS)
//    }

    @Test
    public void testIntList() {
        Observable.range(1, 10)
                .flatMap(v -> Observable.just(v)
                        .delay(11 - v, TimeUnit.SECONDS))
                .toBlocking()
                .subscribe(System.out::println);
    }
    @Test
    public void testRange() {

        Observable<Integer> range = Observable.range(1, 9);
        range.flatMap((s -> Observable.just(s)
                .doOnNext(System.out::println)
                .delay(10 - s, TimeUnit.SECONDS))
        )
                .toBlocking()
                .subscribe(System.out::println);
    }

    @Test
    public void testComposition()
    {
        query("Hello, world!")
                .filter(url -> url != null)
                .flatMap(this::getTitle)
                .take(5)
                .subscribe(System.out::println);    }

    private Observable<String> getTitle(String url)
    {
        return Observable.just(String.format("Title:%s", url));
    }

}