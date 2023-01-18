package com.example.jpa_test;


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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {



    @PostMapping("/peter-topic-produce")
    public void peterTopicSend(){
        Properties props = new Properties();
        props.put("bootstrap.server", "여기에 내 부트스트랩 스트링 넣어라");
        props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        log.info("샌드 프로퍼티 설정 완료");

        Producer<String, String> producer = new KafkaProducer<String, String>(props);
        producer.send(new ProducerRecord<>("peter-topic", "하나"));
        producer.send(new ProducerRecord<>("peter-topic", "둘"));
        producer.send(new ProducerRecord<>("peter-topic", "셋"));
        producer.close();

        log.info("토픽에 메시지 보내기 완료.");
    }




    @GetMapping("/peter-topic-consume")
    public void peterTopicReceive(){
        Properties props = new Properties();
        props.put("bootstrap.servers","여기에 내 부트스트랩 문자열 넣어라");
        props.put("group.id","peter-consumer");
        props.put("enable.auto.commit","true");
        props.put("auto.offset.reset","latest");
        props.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        props.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(props);
        consumer.subscribe(Arrays.asList("peter-topic"));

        try{
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String, String> record : records){
                    System.out.printf("토픽 : %s, 파티션 : %s, 오프셋 : %s, 키 : %s, 밸류 : %s\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }
    }//func






    //----------- PRIVATE HELPER METHODS -----------





    private void initList(List<Long> list){
        for(long i=1; i<=5_000; i++){
            list.add(i);
        }
        Collections.shuffle(list);
    }//func

}























