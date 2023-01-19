package com.example.jpa_test;

import java.util.Arrays;
import java.util.Properties;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@Slf4j
public class JpaTestApplication {

    public static void main(String[] args) {
        SpringApplication.run(JpaTestApplication.class, args);

        KafkaSingletonInstance.listenMessage();

    }//main

}
