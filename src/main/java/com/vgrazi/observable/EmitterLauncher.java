package com.vgrazi.observable;

import com.vgrazi.play.PriceTick;
import com.vgrazi.play.SomeFeed;
import com.vgrazi.play.SomeListener;
import rx.AsyncEmitter;
import rx.Observable;
import rx.observables.ConnectableObservable;

import static com.vgrazi.util.Utils.sleep;

public class EmitterLauncher {
  public static void main(String[] args) {
    SomeFeed<PriceTick> feed = new SomeFeed<>();
    Observable<PriceTick> obs =
      Observable.fromEmitter((AsyncEmitter<PriceTick> emitter) ->
      {
        SomeListener listener = new SomeListener() {
          @Override
          public void priceTick(PriceTick event) {
            emitter.onNext(event);
            if (event.isLast()) {
              emitter.onCompleted();
            }
          }

          @Override
          public void error(Throwable e) {
            emitter.onError(e);
          }
        };
        feed.register(listener);
      }, AsyncEmitter.BackpressureMode.BUFFER);

    ConnectableObservable<PriceTick> hotObservable = obs.publish();
    hotObservable.connect();

    hotObservable.take(10).subscribe((priceTick) ->
      System.out.printf("1 %s %4s %6.2f%n", priceTick.getDate(),
        priceTick.getInstrument(), priceTick.getPrice()));

    sleep(1_000);

    hotObservable.take(10).subscribe((priceTick) ->
      System.out.printf("2 %s %4s %6.2f%n", priceTick.getDate(),
        priceTick.getInstrument(), priceTick.getPrice()));
  }
}
