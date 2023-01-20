package com.example.jpa_test.kafka.consumer;

import com.example.jpa_test.SecretValues;
import com.example.jpa_test.kafka.properties.KafkaProperties;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;

@Slf4j
public class KafkaSingletonConsumer {
    private static final KafkaConsumer<String, String> supportConsumer = new KafkaConsumer<>(
        KafkaProperties.getConsumerProperties());

    //컨슈머가 늘어나면 여기에서 추가한다.

    public static KafkaConsumer<String, String> getSupportConsumer(){
        supportConsumer.subscribe(Arrays.asList(SecretValues.kafkaTopicName));
        return supportConsumer;
    }

}
