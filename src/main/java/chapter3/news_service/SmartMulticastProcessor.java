package chapter3.news_service;

import chapter3.news_service.dto.NewsLetter;
import org.reactivestreams.Processor;
import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class SmartMulticastProcessor implements Processor<NewsLetter,NewsLetter> {

    private NewsLetter cache;
    private Throwable throwable;

    @Override
    public void subscribe(Subscriber<? super NewsLetter> subscriber) {

    }

    @Override
    public void onSubscribe(Subscription subscription) {

    }

    @Override
    public void onNext(NewsLetter newsLetter) {

    }

    @Override
    public void onError(Throwable throwable) {

    }

    @Override
    public void onComplete() {

    }

    private static class InnerSubscripton implements Subscription{
        private final Subscriber<? super NewsLetter> actual;
        final SmartMulticastProcessor parent;

        Throwable throwable;
        boolean done;
        boolean sent;

        volatile long requested;
        static final AtomicLongFieldUpdater<InnerSubscripton> REQUESTED = AtomicLongFieldUpdater.newUpdater(InnerSubscripton.class,"requested");

        volatile int wip;
        static final AtomicIntegerFieldUpdater<InnerSubscripton> WIP = AtomicIntegerFieldUpdater.newUpdater(InnerSubscripton.class,"wip");

        private InnerSubscripton(Subscriber<? super NewsLetter> actual, SmartMulticastProcessor parent) {
            this.actual = actual;
            this.parent = parent;
        }


        @Override
        public void request(long l) {
            if (l <= 0 ){

            }
        }

        @Override
        public void cancel() {

        }

        void tryDrain(){
            if (done){
                return;
            }

            int wip;
            if ((wip = WIP.incrementAndGet(this)) > 1){
                return;
            }

            Subscriber<? super NewsLetter> a = actual;
            long r = requested;

            while (true){
                NewsLetter element = parent.cache;
                if (r > 0 && !sent && element != null){
                    a.onNext(element.withRecipient(getRecipient()));
                    sent = true;
                    r = REQUESTED.decrementAndGet(this);
                }

                wip = WIP.addAndGet(this,-wip);

                if (wip == 0 ){
                    if (!done && isTerminated() && hasMoreEmission()){
                        done = true;
                        if (throwable == null && parent.throwable == null){
                            a.onComplete();
                        }else {
                            throwable = throwable == null ? parent.throwable : throwable;
                            a.onError(throwable);
                        }
                    }
                }
            }
        }

        private boolean hasMoreEmission() {
            return sent || parent.cache == null || throwable != null;
        }


        private boolean isTerminated() {
            return parent.throwable != null || parent.isTerminated();
        }

        private String getRecipient() {
            if (actual instanceof NamedSubscriber){
                return ((NamedSubscriber)actual).getName();
            }
            return null;
        }

        void onError(Throwable t){
            if (done) {
                return;
            }
            tryDrain();
        }

        void onComplete(){
            if (done){
                return;
            }
            parent.remove(this);
            tryDrain();
        }

    }

    private void remove(InnerSubscripton innerSubscripton) {

    }

    private boolean isTerminated() {
        return false;
    }
}
