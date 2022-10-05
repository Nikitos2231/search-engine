package com.example.engine.util;

import com.example.engine.services.MainService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.concurrent.ForkJoinPool;

@Component
public class SaveJoinHtmlEntity {

    private final MainService mainService;

    @Autowired
    public SaveJoinHtmlEntity(MainService mainService) {
        this.mainService = mainService;
    }

    public void saveJoinHtml(String link) {
        long time = System.currentTimeMillis();

        ArrayList<String> links = new ArrayList<>();
        ArrayList<String> code = new ArrayList<>();
        ArrayList<String> html = new ArrayList<>();
        ArrayList<String> fixLinks = new ArrayList<>();
        new ForkJoinPool().invoke(new ForkJoinTest(links, fixLinks, link, code, html, mainService));

        System.out.println(System.currentTimeMillis() - time + "milliseconds");
    }
}
