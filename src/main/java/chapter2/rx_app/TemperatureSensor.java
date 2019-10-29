package chapter2.rx_app;

import org.springframework.stereotype.Component;
import rx.Observable;

import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class TemperatureSensor {
    private final Random random = new Random();
    private final Observable<Temperature> datastream =
            Observable.range(0, Integer.MAX_VALUE)
                    .concatMap(tick -> Observable.just(tick)
                            .delay(random.nextInt(5000), TimeUnit.MILLISECONDS)
                            .map(tickValue -> this.probe()))
                    .publish()
                    .refCount();

    public Observable<Temperature> temperatureStream(){
        return datastream;
    }

    private Temperature probe() {
        return new Temperature(16 + random.nextGaussian() * 10);
    }
}
