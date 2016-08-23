package com.vgrazi.play;

/**
 * Created by victorg on 7/27/2016.
 */
public abstract class SomeListener {
    public abstract void priceTick(PriceTick event);
    public abstract void error(Throwable throwable);

}
