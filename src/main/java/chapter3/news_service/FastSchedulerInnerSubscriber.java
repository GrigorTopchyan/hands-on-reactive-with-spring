package chapter3.news_service;

public class FastSchedulerInnerSubscriber<T> extends SchedulerInnerSubscriber<T> {

    public FastSchedulerInnerSubscriber(SchedulerMainSubscribtion<T> tSchedulerMainSubscribtion) {
        super(tSchedulerMainSubscribtion);
    }

    @Override
    public void onComplete() {

    }

    @Override
    public void onNext(Object o) {
        if (parent.canceled){
            s.cancel();
            return;
        }else {
            parent.emit();
        }
    }
}
