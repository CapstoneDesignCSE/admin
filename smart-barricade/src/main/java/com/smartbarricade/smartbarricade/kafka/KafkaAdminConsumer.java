//package com.smartbarricade.smartbarricade.kafka;
//
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.stereotype.Component;
//
//@Component
//public class KafkaAdminConsumer {
//
//    @KafkaListener(topics = "Smart-Barricade", groupId = "sb")
//    public void handleMessage(String message) {
//        System.out.println("Received message: " + message);
//    }
//}