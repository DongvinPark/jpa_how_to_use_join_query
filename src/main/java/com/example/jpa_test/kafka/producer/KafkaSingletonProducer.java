package com.example.jpa_test.kafka.producer;

import com.example.jpa_test.SecretValues;
import com.example.jpa_test.kafka.properties.KafkaProperties;
import java.util.Arrays;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

public class KafkaSingletonProducer {

    private static final KafkaProducer<String, String> supportProducer = new KafkaProducer<>(
        KafkaProperties.getProducerProperties());

    //컨슈머가 늘어나면 여기에서 추가한다.

    public static Producer<String, String> getSupportProducer(){
        return supportProducer;
    }

}
