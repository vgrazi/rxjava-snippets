package com.vgrazi.play;

import org.junit.Test;
import rx.Observable;

import static com.vgrazi.play.MapFlatMapExamples.Sound.BLANK;
import static com.vgrazi.play.MapFlatMapExamples.Sound.DAH;
import static com.vgrazi.play.MapFlatMapExamples.Sound.DI;
import static rx.Observable.empty;
import static rx.Observable.just;

/**
 * Created by victorg on 7/28/2016.
 */
public class MapFlatMapExamples {
  @Test
  public void testMap() {
    Observable<String> tweets = Observable.just("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
    tweets.map(tweet -> tweet.length()).forEach(System.out::println);
  }

  @Test
  public void testFlatMap() {
    Observable<String> tweets = Observable.just("learning RxJava", "Writing blog about RxJava", "RxJava rocks!!");
    tweets.flatMap(tweet -> Observable.from(tweet.split(""))).forEach(System.out::println);
  }

  Observable<Sound> toMorseCode(char ch) {
    switch (ch) {
      case 'a':
        return just(DI, DAH, BLANK);
      case 'b':
        return just(DAH, DI, DI, DI, BLANK);
      case 'c':
        return just(DAH, DI, DAH, DI, BLANK);
      //...
      case 'p':
        return just(DI, DAH, DAH, DI, BLANK);
      case 'r':
        return just(DI, DAH, DI, BLANK);
      case 's':
        return just(DI, DI, DI, BLANK);
      case 't':
        return just(DAH, BLANK);
      //...
      default:
        return empty();
    }
  }

  @Test
  public void testMorseCode() {
    Observable<Character> just = just('S', 'p', 'a', 'r', 't', 'a');
    Observable<Sound> soundObservable = just
      .map(Character::toLowerCase)
      .flatMap(this::toMorseCode);
    soundObservable.subscribe(System.out::print);
  }

  enum Sound {
    DI, DAH, BLANK;

    @Override
    public String toString() {
      if(this == BLANK) {
        return " ";
      }
      else {
        return super.toString();
      }
    }
  }

}
