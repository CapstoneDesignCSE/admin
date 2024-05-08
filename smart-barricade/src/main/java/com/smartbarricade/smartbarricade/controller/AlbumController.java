package com.smartbarricade.smartbarricade.controller;

import com.smartbarricade.smartbarricade.model.AlbumModel;
import com.smartbarricade.smartbarricade.service.AlbumService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/album")
public class AlbumController {

    @Autowired
    private AlbumService albumService;

    /**
     * Form Controller
     */

    @GetMapping
    public String getAlbum() {
        return "album/index";
    }

    @GetMapping("/detect")
    public String getDetectData(Model model) {

        List<AlbumModel> cachedAlbumDataFromKafka = albumService.getCachedAlbumDataFromKafka();
        model.addAttribute("kafkaData", cachedAlbumDataFromKafka);

        return "album/detect";
    }


    /**
     * API Controller
     */
    @GetMapping("/reset")
    public void resetCachedData() {
        albumService.resetCacheAlbumData();
    }
}
