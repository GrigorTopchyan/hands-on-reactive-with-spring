package chapter3.news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public abstract class SchedulerInnerSubscriber<T> implements Subscriber<T> {
    final SchedulerMainSubscribtion<T> parent;
    Subscription s;

    public SchedulerInnerSubscriber(SchedulerMainSubscribtion<T> parent) {
        this.parent = parent;
    }


    @Override
    public void onSubscribe(Subscription subscription) {
        if (this.s == null){
            this.s = subscription;
            s.request(Long.MAX_VALUE);
        }else {
            s.cancel();
        }
    }

    @Override
    public void onError(Throwable throwable) {
        parent.onError(throwable);
    }

}
