package com.example.blogperformance.dto;

import java.time.LocalDateTime;

public class ArticleWithCommentsStatsDTO {
    private Long id;
    private String title;
    private LocalDateTime publicationDate;
    private int numberOfComments;
    private LocalDateTime mostRecentCommentDate;

    public ArticleWithCommentsStatsDTO() {
    }

    public ArticleWithCommentsStatsDTO(Long id, String title, LocalDateTime publicationDate, int numberOfComments, LocalDateTime mostRecentCommentDate) {
        this.id = id;
        this.title = title;
        this.publicationDate = publicationDate;
        this.numberOfComments = numberOfComments;
        this.mostRecentCommentDate = mostRecentCommentDate;
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

    public int getNumberOfComments() {
        return numberOfComments;
    }

    public void setNumberOfComments(int numberOfComments) {
        this.numberOfComments = numberOfComments;
    }

    public LocalDateTime getMostRecentCommentDate() {
        return mostRecentCommentDate;
    }

    public void setMostRecentCommentDate(LocalDateTime mostRecentCommentDate) {
        this.mostRecentCommentDate = mostRecentCommentDate;
    }
}
