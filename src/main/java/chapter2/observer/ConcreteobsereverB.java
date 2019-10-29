package chapter2.observer;

public class ConcreteobsereverB implements Observer<String> {
    @Override
    public void observe(String event) {
        System.out.println("Observer B: " + event);
    }
}
