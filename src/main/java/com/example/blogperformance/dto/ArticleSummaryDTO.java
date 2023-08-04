package com.example.blogperformance.dto;

import java.time.LocalDateTime;

public class ArticleSummaryDTO {
    private Long id;
    private String title;

    private LocalDateTime publicationDate;

    public ArticleSummaryDTO() {
    }

    public ArticleSummaryDTO(Long id, String title, LocalDateTime publicationDate) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public LocalDateTime getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(LocalDateTime publicationDate) {
        this.publicationDate = publicationDate;
    }
}
