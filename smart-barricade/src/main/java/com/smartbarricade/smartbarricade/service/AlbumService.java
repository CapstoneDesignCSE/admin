package com.smartbarricade.smartbarricade.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smartbarricade.smartbarricade.model.AlbumModel;
import com.smartbarricade.smartbarricade.repository.DetectRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.StringDeserializer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Description;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.serializer.JsonDeserializer;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Service
public class AlbumService {

    @Value("spring.kafka.bootstrap-servers")
    private String BOOTSTRAP_SERVERS;

    @Autowired
    private ValidationService validationService;

    @Autowired
    private DetectRepository detectRepository;

    private AtomicInteger indexer = new AtomicInteger(0);
    private List<AlbumModel> cachedKafkaData = new ArrayList<>();


    @Description("카프카 토픽에 메시지가 발행될 때마다 받아오는 데이터를 로컬 캐싱한다.")
    @Transactional
    @KafkaListener(topics = "Smart-Barricade", groupId = "sb")
    public void cachingFromKafka(String message) throws org.json.simple.parser.ParseException, JsonProcessingException {

        System.out.println("[Get From YoloV8]: " + message);

//        // Yolo 에서 발행한 Json 형식의 메시지를 JsonObject 로 변환
//        JSONParser jsonParser = new JSONParser();
//        Object parsedObjectFromYoloV8 = jsonParser.parse(message);
//        JSONObject parsedJsonFromYoloV8 = (JSONObject) parsedObjectFromYoloV8;

        // JSON 문자열을 Java Map 객체로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        try {
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
                    indexer.getAndIncrement(),
                    detectedId,
                    speed,
                    overSpeed,
                    message
            );

            // 과속 차량 Data 는 DB 에 따로 저장.
            if (overSpeed) {
                detectRepository.save(albumModel);
                System.out.println("[DB Saved: " + albumModel.getId() + "] 과속 차량 확인");
            }

            cachedKafkaData.add(albumModel);
        } catch (IOException e) {
            e.printStackTrace();
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

    public List<AlbumModel> getCachedAlbumDataFromKafka() {
        return cachedKafkaData;
    }

    public void resetCacheAlbumData() {
        cachedKafkaData.clear();
    }
}
