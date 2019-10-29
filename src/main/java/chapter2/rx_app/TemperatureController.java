package chapter2.rx_app;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

@RestController
public class TemperatureController {
    private TemperatureSensor temperatureSensor;

    public TemperatureController(TemperatureSensor temperatureSensor) {
        this.temperatureSensor = temperatureSensor;
    }

    @RequestMapping(value = "/temperature-stream",method = RequestMethod.GET)
    public SseEmitter events(){
        RxSseEmitter emitter = new RxSseEmitter();
        temperatureSensor.temperatureStream().subscribe(emitter.getSubscriber());
        return emitter;
    }
}
