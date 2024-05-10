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

import static jakarta.persistence.GenerationType.SEQUENCE;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class AlbumModel {

    @Id
    @Column(name="id")
    private Long id;

    @Column(name="speed")
    private Integer speed;

    @Column(name="is_danger")
    private Boolean danger;

    @Column(name="message")
    private String message;
}
