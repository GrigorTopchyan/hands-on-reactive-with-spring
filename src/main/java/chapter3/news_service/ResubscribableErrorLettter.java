package chapter3.news_service;

import chapter3.news_service.dto.NewsLetter;
import org.reactivestreams.Subscriber;

public interface ResubscribableErrorLettter {

    void resubscribe(Subscriber<? super NewsLetter> subscriber);
}