package com.vgrazi.observable;

import rx.Observable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.vgrazi.util.Utils.isSlowTime;
import static com.vgrazi.util.Utils.sleep;

public class Launcher
{

    public static void main(String[] args)
    {
        new Launcher().launch();
    }

    private void launch()
    {
        Observable slow = Observable.interval(2, TimeUnit.SECONDS);
        Observable fast = Observable.interval(1, TimeUnit.SECONDS);
        Observable.merge(
                slow.filter(x->isSlowTime()),
                fast.filter(x->!isSlowTime())
        )
        .subscribe(x-> System.out.println(new Date()))
        ;

        sleep(50_000);
    }
}
