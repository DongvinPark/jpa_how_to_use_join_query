package com.example.jpa_test;


import com.example.jpa_test.kafka.producer.KafkaSingletonProducer;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {



    @PostMapping("/peter-topic-produce/{input}")
    public void peterTopicSend(
        @PathVariable String input
    ){
        Producer<String, String> producer = KafkaSingletonProducer.getSupportProducer();
        producer.send(new ProducerRecord<>(SecretValues.kafkaTopicName, input));

        log.info("토픽에 메시지 보내기 완료.");
    }



}























