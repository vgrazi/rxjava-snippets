package com.vgrazi.play;

import org.junit.Test;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Func0;
import rx.functions.Func1;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseMotionListener;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * Created by victorg on 8/2/2016.
 */
public class BufferThrottleExample {
    private final JPanel panel = new JPanel();
    @Test
    public void mouseClickTest() throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Test");
        panel.add(label);
        panel.setBackground(Color.yellow);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        Observable<String> mouseObservable = Observable.create(subscriber -> {
            panel.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    subscriber.onNext(String.format("Clicked at %d, %d", e.getX(), e.getY()));
                }
            });
        });

        Subscription subscription = mouseObservable
                .debounce(400, TimeUnit.MILLISECONDS)
//                .window(400, TimeUnit.MILLISECONDS)
//                .toList()
                .doOnNext((x) -> System.out.println(new Date() + x.toString()))
//                .filter(list -> !list.isEmpty())
//                .map(list -> list.get(list.size() - 1))
                .subscribe((text) -> label.setText(text.toString()));
        Thread.sleep(10_000);
    }

    @Test
    public void mouseMoveTest() throws InterruptedException {
        JFrame frame = new JFrame();
        frame.setBounds(100, 100, 500, 500);
        frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        JLabel label = new JLabel("Test");
        panel.add(label);
        panel.setBackground(Color.yellow);
        frame.getContentPane().add(panel);
        frame.setVisible(true);

        Observable<String> observable = Observable.create(subscriber -> {
            panel.addMouseMotionListener(new MouseMotionListener() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    subscriber.onNext(String.format("Dragged to %d, %d", e.getX(), e.getY()));
                }

                @Override
                public void mouseMoved(MouseEvent e) {
                    subscriber.onNext(String.format("Moved to %d, %d", e.getX(), e.getY()));
                }
            });
        });

        observable.buffer(new Func0<Observable<String>>() {
            @Override
            public Observable<String> call() {
                return Observable.just(new Date() + " Hit");
            }
        })
                .doOnNext(System.out::println)
                .debounce(500, TimeUnit.MILLISECONDS)
                .subscribe(System.out::println);

//        observable.buffer(100, TimeUnit.MILLISECONDS)
//                .doOnNext(System.out::println)
//                .filter(list -> !list.isEmpty())
//                .map(list -> list.get(list.size() - 1))
//                .subscribe(label::setText);
        Thread.sleep(100_000);
    }
    @Test
    public void test() throws InterruptedException {
        Observable.interval(100, TimeUnit.MILLISECONDS)
                .buffer(Observable.interval(500, TimeUnit.MILLISECONDS))
                .doOnNext(System.out::println)
                .map(Collections::max)
                .subscribe(System.out::println);

        Thread.sleep(100_000);

    }
}
