package com.example.locationproducersvc.service;

import com.example.locationproducersvc.dto.MessageDetails;
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

    public Mono<MessageDetails> send(MessageDetails messageDetails){
        log.info("send to topic={}, {}={},", topic, String.class.getSimpleName(), messageDetails.getMsg());
        return reactiveKafkaProducerTemplate.send(topic, messageDetails.getMsg())
                .doOnSuccess(senderResult -> log.info("sent {} offset : {}", messageDetails.getMsg(), senderResult.recordMetadata().offset()))
                .doOnError(error-> log.info("Error occurred during sending message to kafka :{} . Error is : {}", error ))
                .map(test->messageDetails);
    }
}
