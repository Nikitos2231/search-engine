package com.example.engine.services;

import com.example.engine.model.HtmlEntity;
import com.example.engine.repositories.HtmlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MainService {

    private final HtmlRepository htmlRepository;

    @Autowired
    public MainService(HtmlRepository htmlRepository) {
        this.htmlRepository = htmlRepository;
    }

    public void save(HtmlEntity htmlEntity) {
        htmlRepository.save(htmlEntity);
    }

    public boolean findByPath(String path) {
        return htmlRepository.existsHtmlEntitiesByPath(path);
    }
}
