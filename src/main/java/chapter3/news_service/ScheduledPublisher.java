package chapter3.news_service;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledPublisher<T> implements Publisher<T> {
    final ScheduledExecutorService scheduledExecutorService;

    final int period;
    final TimeUnit unit;
    final Callable<? extends Publisher<T>> publisherCallable;

    public ScheduledPublisher(
            Callable<? extends Publisher<T>> publisherCallable,
            int period,
            TimeUnit unit,
            ScheduledExecutorService scheduledExecutorService) {
        this.scheduledExecutorService = scheduledExecutorService;
        this.period = period;
        this.unit = unit;
        this.publisherCallable = publisherCallable;
    }


    public ScheduledPublisher(Callable<? extends Publisher<T>> publisherCallable, int period, TimeUnit unit) {
        this(publisherCallable,period,unit, Executors.newSingleThreadScheduledExecutor());
    }

    @Override
    public void subscribe(Subscriber<? super T> subscriber) {

    }

    static final class ScheduledMaonSubscribtion<T> implements Subscription,Runnable{

        @Override
        public void run() {

        }

        @Override
        public void request(long l) {

        }

        @Override
        public void cancel() {

        }
    }
}
