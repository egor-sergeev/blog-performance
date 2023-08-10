package com.example.blogperformance.dto;

import com.example.blogperformance.model.Article;

import java.time.LocalDateTime;

public class ArticleDTO {
    private final Long id;
    private final String title;
    private final LocalDateTime publicationDate;

    public ArticleDTO(Article article) {
        this.id = article.getId();
        this.title = article.getTitle();
        this.publicationDate = article.getPublicationDate();
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }
}
