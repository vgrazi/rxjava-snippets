package com.vgrazi.observable;

import rx.Observable;

import java.util.Arrays;
import java.util.List;

public class WordLauncher
{

    public static void main(String[] args)
    {
        new WordLauncher().launch();
    }

    private void launch()
    {
        List<String> words = Arrays.asList(
                "the",
                "quick",
                "brown",
                "fox",
                "jumped",
                "over",
                "the",
                "lazy",
                "dogs"
        );

        Observable.from(words)
                .flatMap(word -> Observable.from(word.split("")))
                .distinct()
                .sorted()
                .zipWith(Observable.range(1, Integer.MAX_VALUE),
                        (string, count)->String.format("%2d. %s", count, string))
                .subscribe(System.out:: println);
    }
}
