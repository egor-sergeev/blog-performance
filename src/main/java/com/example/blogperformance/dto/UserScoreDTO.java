package com.example.blogperformance.dto;

import com.example.blogperformance.model.Article;

import java.util.List;
import java.util.stream.Collectors;

public class UserScoreDTO {
    private final Long id;
    private final String name;
    private final int score;
    private final List<Article> articles;

    public UserScoreDTO(Long id, String name, int score, List<Article> articles) {
        this.id = id;
        this.name = name;
        this.score = score;
        this.articles = articles.stream()
                .peek(article -> article.setContent(article.getContent().repeat(10)))
                .collect(Collectors.toList());
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

    public List<ArticleDTO> getArticles() {
        return articles.stream()
                .limit(1)
                .map(ArticleDTO::new)
                .collect(Collectors.toList());
    }
}
