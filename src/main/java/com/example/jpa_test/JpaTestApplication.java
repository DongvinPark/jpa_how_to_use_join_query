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

        Properties propsConsume = new Properties();
        propsConsume.put("bootstrap.servers","여기에 내 부트스트랩 문자열 넣어라");
        propsConsume.put("group.id","peter-consumer");
        propsConsume.put("enable.auto.commit","true");
        propsConsume.put("auto.offset.reset","latest");
        propsConsume.put("key.deserializer","org.apache.kafka.common.serialization.StringDeserializer");
        propsConsume.put("value.deserializer","org.apache.kafka.common.serialization.StringDeserializer");

        log.info("컨슘 프로퍼티 설정 완료");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(propsConsume);
        consumer.subscribe(Arrays.asList("peter-topic"));

        try{
            log.info("메시지 확인 및 확인 하면서 해야 할 일 시작");
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String, String> record : records){
                    log.info("컨슈머 레코드 출력작업 진행");
                    System.out.printf("토픽 : %s, 파티션 : %s, 오프셋 : %s, 키 : %s, 밸류 : %s\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());
                }
            }
        } finally {
            consumer.close();
        }

    }//main

}
