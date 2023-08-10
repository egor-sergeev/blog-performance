package com.example.blogperformance.dto;

import com.example.blogperformance.model.Article;

import java.util.List;
import java.util.stream.Collectors;

public class UserScoreDTO {
    private Long id;
    private String name;
    private int score;

    private List<Article> articles;

    public UserScoreDTO(Long id, String name, int score, List<Article> articles) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.articles = articles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public List<ArticleSummaryDTO> getArticles() {
        return articles.stream()
                .map(this::convertToSummary)
                .collect(Collectors.toList());
    }

    private ArticleSummaryDTO convertToSummary(Article article) {
        return new ArticleSummaryDTO(article.getId(), article.getTitle(), article.getPublicationDate());
    }
}
