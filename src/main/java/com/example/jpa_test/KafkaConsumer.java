package com.example.jpa_test;

import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class KafkaConsumer {

    @KafkaListener(
        topics = "msk",
        groupId = "demo"
    )
    public void listen(String msg) throws IOException {
        log.info("컨슘 메시지 : {}", msg);
    }

}
