package com.example.locationproducersvc.config;

import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import reactor.kafka.sender.SenderOptions;

import java.util.Map;

@Configuration
public class Config {

    @Value(value = "${kafka.security.protocol}")
    private String securityProtocol;
    @Value(value = "${kafka.sasl.jaas.config}")
    private String saslJassConfig;
    @Value(value = "${kafka.sasl.mechanism}")
    private String saslMechanism;

    @Bean
    public ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate(
            KafkaProperties properties) {
        Map<String, Object> props = properties.buildProducerProperties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-6ojv2.us-west4.gcp.confluent.cloud:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put("security.protocol", securityProtocol);
        props.put("sasl.mechanism", saslMechanism);
        props.put("sasl.jaas.config", saslJassConfig);

        return new ReactiveKafkaProducerTemplate<String, String>(SenderOptions.create(props));
    }
}
