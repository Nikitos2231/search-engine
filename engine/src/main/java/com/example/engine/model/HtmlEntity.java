package com.example.engine.model;

import javax.persistence.*;

@Entity
@Table(name = "html")
public class HtmlEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int htmlId;

    @Column(name = "path")
    private String path;

    @Column(name = "code")
    private int code;

    @Column(name = "content")
    private String content;

    public HtmlEntity(int htmlId, String path, int code, String content) {
        this.htmlId = htmlId;
        this.path = path;
        this.code = code;
        this.content = content;
    }

    public HtmlEntity() {
    }

    public int getHtmlId() {
        return htmlId;
    }

    public void setHtmlId(int htmlId) {
        this.htmlId = htmlId;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
