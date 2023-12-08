package com.example.locationproducersvc.service;

import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.reactive.ReactiveKafkaProducerTemplate;

@ExtendWith(MockitoExtension.class)
class ReactiveProducerServiceTest {

    @Mock
    private ReactiveKafkaProducerTemplate<String, String> reactiveKafkaProducerTemplate;

    @InjectMocks
    private ReactiveProducerService reactiveProducerService;

    /*@Test
    void testSend() {
        String testTopic = "test-topic";
        String fileName = "test-file";
        String testMessage = "Test message";
        MessageDetails messageDetails = new MessageDetails(fileName, testMessage);

        when(reactiveKafkaProducerTemplate.send(any(), any()))
                .thenReturn(Mono.just(new SenderResult<>(null, true, 0, 0, null, null)));

        StepVerifier.create(reactiveProducerService.send(messageDetails))
                .expectNext(messageDetails) // Assuming you expect the same messageDetails to be emitted
                .verifyComplete();
    }*/
}
