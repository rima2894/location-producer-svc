package com.example.locationproducersvc.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

@Slf4j
@Service
public class ReactiveProducerService {

    private final ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;
    private String topic;

    public ReactiveProducerService(ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate,
                                   @Value(value = "${kafka.json.location.topic}") String topic) {
        this.reactiveKafkaProducerTemplate = reactiveKafkaProducerTemplate;
        this.topic = topic;
    }

    public Mono<String> send(String locationJson) throws RuntimeException{
        log.info("send to topic={}, {}={},", topic, String.class.getSimpleName(), locationJson);
        return reactiveKafkaProducerTemplate.send(topic, locationJson)
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", locationJson, senderResult.recordMetadata().offset()))
                .doOnError(error-> log.info("Error occurred during sending message to kafka :{} . Error is : {}", error ))
                .map(test->locationJson);
    }
}
