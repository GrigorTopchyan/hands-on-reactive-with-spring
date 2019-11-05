package chapter3.news_service;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.*;


public class ScheduledPublisher<T> implements Publisher<T> {
    private final ScheduledExecutorService scheduledExecutorService;

    private final int period;
    private final TimeUnit unit;
    private final Callable<? extends Publisher<T>> publisherCallable;

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
    public void subscribe(Subscriber<? super T> actual) {
        SchedulerMainSubscribtion<T> s = new SchedulerMainSubscribtion<>(actual,publisherCallable);
        actual.onSubscribe(s);
        s.setScheduledFuture(scheduledExecutorService.scheduleWithFixedDelay(s,0,period,unit));
    }
}
