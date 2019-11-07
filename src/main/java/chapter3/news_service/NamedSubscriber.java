package chapter3.news_service;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

public class NamedSubscriber<T> implements Subscriber<T> {
    @Override
    public void onSubscribe(Subscription subscription) {

    }

    @Override
    public void onNext(T t) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    public String getName() {
        return null;
    }
}
