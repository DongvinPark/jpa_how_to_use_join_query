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
import org.apache.kafka.clients.producer.ProducerRecord;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class Controller {

    private final com.example.jpa_test.KafkaProducer kafkaProducer;




    @PostMapping("/kafka-run")
    public String sendMsg(
        @RequestParam String message
    ){
        kafkaProducer.sendMessage(message);
        return "카프카 메시지 전송 성공";
    }





    @GetMapping("/kafka-producer-run")
    public void connectKafkaProducer(){

        final String TOPIC = "msk_test";

        Properties configs = new Properties();
        configs.put("bootstrap.servers", "b-2.soicaltodobackend.2f4dqe.c2.kafka.ap-northeast-2.amazonaws.com:9092,b-1.soicaltodobackend.2f4dqe.c2.kafka.ap-northeast-2.amazonaws.com:9092");
        configs.put("acks", "all");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaProducer<String, String> producer = new KafkaProducer<String, String>(configs);

        for(int i=0; i<5; i++){
            String message = "hello" + i;
            producer.send(new ProducerRecord<>(TOPIC, message));
        }

        producer.flush();
        producer.close();

    }//end of connectKafka






    @GetMapping("/kafka-consumer-run")
    public void connectKafkaConsumer(){

        final String TOPIC = "msk_test";

        Properties configs = new Properties();
        configs.put("bootstrap.servers", "b-2.soicaltodobackend.2f4dqe.c2.kafka.ap-northeast-2.amazonaws.com:9092,b-1.soicaltodobackend.2f4dqe.c2.kafka.ap-northeast-2.amazonaws.com:9092");
        configs.put("acks", "all");
        configs.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
        configs.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<String, String>(configs);

        consumer.subscribe(Arrays.asList(TOPIC));

        boolean stopper = false;
        while(!stopper){
            ConsumerRecords<String, String> records = consumer.poll(500);
            for(ConsumerRecord<String, String> record : records){
                System.out.println("record.value() = " + record.value());
            }
            stopper = true;
        }

    }//end of connectKafka






    //----------- PRIVATE HELPER METHODS -----------





    private void initList(List<Long> list){
        for(long i=1; i<=5_000; i++){
            list.add(i);
        }
        Collections.shuffle(list);
    }//func

}























