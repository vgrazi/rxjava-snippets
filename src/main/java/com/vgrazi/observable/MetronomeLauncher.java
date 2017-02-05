package com.vgrazi.observable;


import io.reactivex.Observable;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import static com.vgrazi.util.Utils.isSlowTime;
import static com.vgrazi.util.Utils.sleep;

public class MetronomeLauncher
{

    public static void main(String[] args)
    {
        new MetronomeLauncher().launch();
    }

    private void launch()
    {
        Observable fast = Observable.interval(1, TimeUnit.SECONDS);
        Observable slow = Observable.interval(2, TimeUnit.SECONDS);
        final int[] i = {1};
        Observable.merge(
                fast.filter(x -> !isSlowTime()),
                slow.filter(x -> isSlowTime())
        )
                .subscribe(x -> System.out.println(i[0]++ + " " + new Date()))
        ;
        sleep(50_000);
    }
}
