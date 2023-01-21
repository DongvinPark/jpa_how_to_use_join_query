package com.example.jpa_test;

import org.springframework.stereotype.Service;

@Service
public class ServiceInstance {

    public void work(){
        while(true){
            if(!MessageContainer.staticMessageQueue.isEmpty()){
                System.out.println("메시지 큐 읽기");
                System.out.println("메시지 큐 값 : " + MessageContainer.staticMessageQueue.pollFirst());
            }
        }
    }

}
