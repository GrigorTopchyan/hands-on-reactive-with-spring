package chapter3.news_service;

import java.util.concurrent.atomic.AtomicLongFieldUpdater;

public class SubscriptionUtils {

    private SubscriptionUtils() {
    }

    public static long addCap (long current, long requested){
        long cap = current + requested;
        if (cap < 0){
            cap = Long.MAX_VALUE;
        }
        return cap;
    }
    @SuppressWarnings("unchecked")
    public static long request(long n, Object instance, AtomicLongFieldUpdater updater){
        while (true){
            long currentDemand = updater.get(instance);
            if (currentDemand == Long.MAX_VALUE){
                return currentDemand;
            }

            long adjustedDemand = addCap(currentDemand,n);
            if (updater.compareAndSet(instance,currentDemand,adjustedDemand)){
                return currentDemand;
            }
        }
    }
}
