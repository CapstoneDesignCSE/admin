package com.smartbarricade.smartbarricade.service;

import org.springframework.stereotype.Service;

@Service
public class ValidationService {

    // 안전과 승차감을 위해서는 15~20km/h 이하로 통과해야 하고 이는 규정 속도인 30km/h에 못 미친다.
    private static final int MAX_SPEED = 30;

    // 과속 Validation.
    public boolean isOverSpeed(int speed) {
        return speed > MAX_SPEED;
    }
    // 정지 Validation. 추가한 이유는 차량이 우회전 구간 멈춰 있을 시 경고등 작동 등과 같은 옵션 제공위해.
    public boolean isStoppedBeforeEnter(int speed) {
        return speed <= 0;
    }
}
