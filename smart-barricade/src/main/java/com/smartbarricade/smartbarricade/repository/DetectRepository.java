package com.smartbarricade.smartbarricade.repository;

import com.smartbarricade.smartbarricade.model.AlbumModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetectRepository extends JpaRepository<AlbumModel, String> {

}
