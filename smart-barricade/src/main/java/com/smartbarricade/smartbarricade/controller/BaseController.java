package com.smartbarricade.smartbarricade.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/base")
public class BaseController {

    @GetMapping(value = "/index")
    public String indexPage() {
        return "/view/index";
    }

}
