package chapter3.news_service;

import chapter3.news_service.dto.News;
import chapter3.news_service.dto.NewsLetter;
import com.mongodb.reactivestreams.client.MongoClient;
import com.mongodb.reactivestreams.client.MongoCollection;
import org.reactivestreams.Publisher;
import org.reactivestreams.Subscriber;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class NewsServicePublisher implements Publisher<NewsLetter> {

    final SmartMulticastProcessor processor;

    public NewsServicePublisher(MongoClient client, String categoryOfIntegers) {
        MongoCollection<News> collection = client.getDatabase("news").getCollection("news", News.class);
        DbPublisher dbPublisher = new DbPublisher(collection, categoryOfIntegers);
        ScheduledPublisher<NewsLetter> scheduler = new ScheduledPublisher<>(
                () -> new NewsPreparationOperator(dbPublisher, "Some Digest"), 1, TimeUnit.DAYS
        );

        SmartMulticastProcessor processor = new SmartMulticastProcessor();
        scheduler.subscribe(processor);
        this.processor = processor;
    }

    public NewsServicePublisher(Consumer<SmartMulticastProcessor> setup){
        this.processor = new SmartMulticastProcessor();
        setup.accept(processor);
    }

    @Override
    public void subscribe(Subscriber<? super NewsLetter> subscriber) {
        processor.subscribe(subscriber);
    }
}
