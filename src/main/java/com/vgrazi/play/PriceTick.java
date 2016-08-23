package com.vgrazi.play;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class PriceTick {
  private final Date date;
  private final String instrument;
  private final double price;
  private final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("hh:mm:ss.SSS");


  public PriceTick(Date date, String instrument, double price) {
    this.date = date;
    this.instrument = instrument;
    this.price = price;
  }

  public Date getDate() {
    return date;
  }

  public String getInstrument() {
    return instrument;
  }

  public double getPrice() {
    return price;
  }

  @Override
  public String toString() {
    return String.format("%s %s %s", DATE_FORMAT.format(new Date()), instrument, price);
  }
}
