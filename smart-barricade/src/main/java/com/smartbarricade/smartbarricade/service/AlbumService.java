package com.smartbarricade.smartbarricade.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbarricade.smartbarricade.model.AlbumModel;
import com.smartbarricade.smartbarricade.repository.DetectRepository;
import jakarta.transaction.Transactional;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;

@Slf4j
@Service
@ConfigurationProperties(prefix = "custom.admin.album.data")
public class AlbumService {

    @Value("spring.kafka.bootstrap-servers")
    private String BOOTSTRAP_SERVERS;

    private final Integer MAX_DETECT_DATA = 50;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DetectRepository detectRepository;

    private final Map<String, AlbumModel> cachedKafkaDataMap = new TreeMap<>();


    @Description("카프카 토픽에 메시지가 발행될 때마다 받아오는 데이터를 로컬 캐싱한다.")
    @Transactional
    @KafkaListener(topics = "Smart-Barricade", groupId = "sb")
    public void cachingFromKafka(String message) {

        System.out.println("[Get From YoloV8]: " + message);

        // JSON 문자열을 Java Map 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            if (cachedKafkaDataMap.size() > MAX_DETECT_DATA) {
                cachedKafkaDataMap.clear();
            }

            Map<String, String> kafkaJsonData = objectMapper.readValue(message, HashMap.class);

            // 변환된 객체 사용 예시
            String detectedId = kafkaJsonData.get("id");
            String detectedSpeed = kafkaJsonData.get("speed");
            int speed = (int) Double.parseDouble(detectedSpeed);

            // 속도 유효성 검증
            boolean overSpeed = validationService.isOverSpeed(speed);

            // 차량 우회전 구간 정지 여부 - 추후 경고등 옵션 추가?
            boolean stoppedBeforeEnter = validationService.isStoppedBeforeEnter(speed);

            AlbumModel albumModel = new AlbumModel(
                    Long.parseLong(detectedId),
                    speed,
                    overSpeed,
                    message
            );

            if (overSpeed) {
                detectRepository.save(albumModel);
            }

            // 과속 차량 Data 는 DB 에 따로 저장.
            if (overSpeed) {
                System.out.println("[DB Saved: " + albumModel.getId() + "] 과속 차량 확인");
            }

            cachedKafkaDataMap.put(detectedId, albumModel);
        } catch (IOException e) {
            log.error("[Kafka Error] ::: 캐싱 실패" + e.getMessage());
        }
    }

    // Kafka consumer configuration
    @Bean
    public Map<String, Object> consumerConfigs() {
        Map<String, Object> props = new HashMap<>();
        props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, BOOTSTRAP_SERVERS);
        props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, StringDeserializer.class);
        props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, JsonDeserializer.class);
        props.put(JsonDeserializer.TRUSTED_PACKAGES, "*");
        return props;
    }

    public Map<String, AlbumModel> getCachedAlbumDataFromKafka() {
        return cachedKafkaDataMap;
    }

    public void resetCacheAlbumData() {
        cachedKafkaDataMap.clear();
    }
}
