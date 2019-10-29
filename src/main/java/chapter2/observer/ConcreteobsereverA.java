package chapter2.observer;

public class ConcreteobsereverA implements Observer<String> {
    @Override
    public void observe(String  event) {
        System.out.println("Observer A: " + event);
    }
}
