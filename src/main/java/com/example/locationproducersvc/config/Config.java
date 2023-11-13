package com.example.locationproducersvc.config;

import com.azure.storage.blob.BlobClientBuilder;
import com.azure.storage.blob.BlobContainerClient;
import com.azure.storage.blob.BlobServiceClient;
import com.azure.storage.blob.BlobServiceClientBuilder;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.common.serialization.StringSerializer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.kafka.KafkaProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;
import org.springframework.kafka.support.serializer.JsonSerializer;
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
        //props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "pkc-lzvrd.us-west4.gcp.confluent.cloud:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, StringSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, JsonSerializer.class);
        props.put("security.protocol", securityProtocol);
        props.put("sasl.mechanism", saslMechanism);
        props.put("sasl.jaas.config", saslJassConfig);

        return new ReactiveKafkaProducerTemplate<String, String>(SenderOptions.create(props));
    }

    @Bean
    public BlobContainerClient getContainerForRead (@Value("${app.blob.connection-string}") final String connectionString,
                                                     @Value("${app.blob.source.container-name}") final String containerName) {
        BlobServiceClient blobServiceClient = new BlobServiceClientBuilder().connectionString(connectionString).buildClient();
        return blobServiceClient.getBlobContainerClient(containerName);
    }

    @Bean
    @Qualifier("sourceBlobContainer")
    public BlobClientBuilder getContainerForDownload(@Value("${app.blob.connection-string}") final String connectionString,
                                                @Value("${app.blob.source.container-name}") final String containerName) {
        BlobClientBuilder client = new BlobClientBuilder();
        client.connectionString(connectionString);
        client.containerName(containerName);
        return client;
    }

    @Bean
    @Qualifier("archiveBlobContainer")
    public BlobClientBuilder getContainerForArchive(@Value("${app.blob.connection-string}") final String connectionString,
                                                    @Value("${app.blob.archive.container-name}") final String containerName) {
        BlobClientBuilder client = new BlobClientBuilder();
        client.connectionString(connectionString);
        client.containerName(containerName);
        return client;
    }
}
