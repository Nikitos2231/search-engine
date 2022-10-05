package com.example.engine.controllers;

import com.example.engine.services.MainService;
import com.example.engine.util.SaveJoinHtmlEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {



    private final MainService mainService;
    private final SaveJoinHtmlEntity saveJoinHtmlEntity;

    @Autowired
    public MainController(MainService mainService, SaveJoinHtmlEntity saveJoinHtmlEntity) {
        this.mainService = mainService;
        this.saveJoinHtmlEntity = saveJoinHtmlEntity;
    }

    @GetMapping("/main")
    public String getMainPage() {
        return "main";
    }

    @PostMapping("/main/add_link")
    public String insertAddressLink(@RequestParam("link") String link) {
        long time = System.currentTimeMillis();
        saveJoinHtmlEntity.saveJoinHtml(link);
        System.out.println(System.currentTimeMillis() - time);
        return "redirect:/main";
    }
}
