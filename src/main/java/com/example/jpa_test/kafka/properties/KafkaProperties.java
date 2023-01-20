package com.example.jpa_test.kafka.properties;

import com.example.jpa_test.SecretValues;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class KafkaProperties {

    public static Properties getConsumerProperties(){
        Properties consumerProperty = new Properties();
        consumerProperty.put("bootstrap.servers", SecretValues.kafkaBootstrapServerAddr);
        consumerProperty.put("group.id","peter-consumer");
        consumerProperty.put("enable.auto.commit","true");
        consumerProperty.put("auto.offset.reset","latest");
        consumerProperty.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        consumerProperty.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        log.info("컨슘 프로퍼티 설정 완료");
        return consumerProperty;
    }



    public static Properties getProducerProperties(){
        Properties producerProperty = new Properties();
        producerProperty.put("bootstrap.servers", SecretValues.kafkaBootstrapServerAddr);
        producerProperty.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        producerProperty.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        log.info("샌드 프로퍼티 설정 완료");
        return producerProperty;
    }

}
