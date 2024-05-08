package com.smartbarricade.smartbarricade.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.Value;
import org.springframework.boot.context.properties.bind.DefaultValue;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AlbumModel {

    //TODO: 추후 메시지 내 데이터 역할 구분하여 모델 작성 할 것.
    private Integer index;

    @Id
    @GeneratedValue
    private String id;

    private Integer speed;
    private Boolean danger;
    private String message;
}
