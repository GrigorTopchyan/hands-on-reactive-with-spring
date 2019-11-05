package chapter3.news_service;

import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

final class SchedulerMainSubscribtion<T> implements Subscription,Runnable{

        private final Subscriber<? super T> actual;
        private final Callable<? extends Publisher<T>> publisherCallable;

        private ScheduledFuture<?> scheduledFuture;

        private volatile long requested;
        private static final AtomicLongFieldUpdater<SchedulerMainSubscribtion> REQUESTED = AtomicLongFieldUpdater
                .newUpdater(SchedulerMainSubscribtion.class,"requested");
        volatile boolean canceled;

        SchedulerMainSubscribtion(Subscriber<? super T> actual, Callable<? extends Publisher<T>> publisherCallable) {
            this.actual = actual;
            this.publisherCallable = publisherCallable;
        }


        @Override
        public void run() {
            if (!canceled){
                try {
                    Publisher<T> innerPublisher = Objects.requireNonNull(publisherCallable.call());
                    if (requested == Long.MAX_VALUE){
                        innerPublisher.subscribe(new FastSchedulerInnerSubscriber<>(this));
                    }else {
                        innerPublisher.subscribe(new SlowSchedulerInnreSubscriber<>(this));
                    }
                } catch (Exception e) {
                    onError(e);
                }
            }
        }

        @Override
        public void request(long l) {
            if (l < 0){
                onError(new IllegalArgumentException("Spec. Rule 3.9 - Cannot request a non srrictly positive number " + l));
                return;
            }
            SubscriptionUtils.request(l,this,REQUESTED);
        }


        public void setScheduledFuture(ScheduledFuture<?> scheduledFuture) {
            this.scheduledFuture = scheduledFuture;
        }

        public void onError(Throwable e) {
            if (canceled){
               return;
            }
            cancel();
            actual.onError(e);
        }

        @Override
        public void cancel() {
            if (!canceled){
                canceled = true;
                if (scheduledFuture != null){
                    scheduledFuture.cancel(true);
                }
            }
        }

    public void tryEmit(T e) {
        while (true){
            long r = requested;
            if (r <= 0){
                onError(new IllegalStateException("Lack of demand"));
                return;
            }

            if (requested == Long.MAX_VALUE || REQUESTED.compareAndSet(this,r,r -1)) {
                emit(e);
            }
        }
    }

    public void emit(T e) {
            this.actual.onNext(e);
    }
}