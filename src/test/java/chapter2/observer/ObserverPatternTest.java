package chapter2.observer;

import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.mockito.Mockito.times;

public class ObserverPatternTest {
    @Test
    public void observersHandleEventsFromSubject(){
        //given
        Subject<String> subject = new ConcreteSubject();
        Observer<String> observerA = Mockito.spy(new ConcreteobsereverA());
        Observer<String> observerB = Mockito.spy(new ConcreteobsereverB());

        subject.notifyObservers("No Listeners");
        subject.registerObserver(observerA);
        subject.notifyObservers("Message for A");

        subject.registerObserver(observerB);
        subject.notifyObservers("Message for A & B");

        subject.unregisterObserver(observerA);
        subject.notifyObservers("Message for B");

        subject.unregisterObserver(observerB);
        subject.notifyObservers("No listeners");

        //then
        Mockito.verify(observerA, times(1)).observe("Message for A");
        Mockito.verify(observerA, times(1)).observe("Message for A & B");
        Mockito.verifyNoMoreInteractions(observerA);

        Mockito.verify(observerB, times(1)).observe("Message for B");
        Mockito.verify(observerB, times(1)).observe("Message for A & B");
        Mockito.verifyNoMoreInteractions(observerB);

    }
}
