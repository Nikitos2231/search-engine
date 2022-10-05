package com.example.engine.repositories;

import com.example.engine.model.HtmlEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HtmlRepository extends JpaRepository<HtmlEntity, Integer> {

    HtmlEntity findByPath(String path);

    boolean existsHtmlEntitiesByPath(String path);


}
