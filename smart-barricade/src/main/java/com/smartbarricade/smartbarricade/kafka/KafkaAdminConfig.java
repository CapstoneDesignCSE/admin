//package com.smartbarricade.smartbarricade.kafka;
//
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.kafka.annotation.EnableKafka;
//import org.apache.kafka.clients.consumer.ConsumerConfig;
//import org.apache.kafka.common.serialization.StringDeserializer;
//import org.springframework.context.annotation.Bean;
//import org.springframework.kafka.core.ConsumerFactory;
//import org.springframework.kafka.core.DefaultKafkaConsumerFactory;
//import org.springframework.kafka.core.KafkaTemplate;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@EnableKafka
//@Configuration
//public class KafkaAdminConfig {
//
//    // Kafka Consumer는 Kafka 토픽에서 메시지를 읽어오는 역할을 담당.
//    // 이러한 Kafka Consumer를 생성하고 구성하기 위해 ConsumerFactory를 사용.
//
//    @Value("${spring.kafka.bootstrap-servers}")
//    private static String BOOTSTRAP_SERVERS;
//
//    @Bean
//    public ConsumerFactory<String, String> consumerFactory() {
//        Map<String, Object> props = new HashMap<>();
//        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
//        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
//        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
////        props.put(ConsumerConfig.GROUP_ID_CONFIG, "my-group");
//
//        return new DefaultKafkaConsumerFactory<>(props);
//    }
//}