package com.example.jpa_test;

import com.example.jpa_test.kafka.consumer.KafkaSingletonConsumer;
import java.util.Arrays;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

@Slf4j
public class KafkaListeners {

    public static void listenSupportMessage(){

        KafkaConsumer<String, String> consumer = KafkaSingletonConsumer.getSupportConsumer();

        try{
            log.info("서포트 메시지 확인 및 확인 하면서 해야 할 일 시작");
            while(true){
                ConsumerRecords<String, String> records = consumer.poll(100);
                for(ConsumerRecord<String, String> record : records){
                    log.info("컨슈머 레코드 출력작업 진행");
                    System.out.printf("토픽 : %s, 파티션 : %s, 오프셋 : %s, 키 : %s, 밸류 : %s\n", record.topic(), record.partition(), record.offset(), record.key(), record.value());

                    MessageContainer.staticMessageQueue.addLast(record.value());
                }//for
            }
        } finally {
            consumer.close();
        }
    }

}
