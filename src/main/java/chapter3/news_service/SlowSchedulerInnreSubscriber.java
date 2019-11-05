package chapter3.news_service;

public class SlowSchedulerInnreSubscriber<T> extends SchedulerInnerSubscriber<T> {

    public SlowSchedulerInnreSubscriber(SchedulerMainSubscribtion<T> tSchedulerMainSubscribtion) {
        super(tSchedulerMainSubscribtion);
    }


    @Override
    public void onNext(T t) {
        if (parent.canceled){
            s.cancel();
            return;
        }
        parent.tryEmit();
    }

    @Override
    public void onError(Throwable throwable) {
        parent.onError(throwable);
    }

    @Override
    public void onComplete() {

    }

}
