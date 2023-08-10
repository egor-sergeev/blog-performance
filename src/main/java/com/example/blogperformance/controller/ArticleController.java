package com.example.blogperformance.controller;

import com.example.blogperformance.dto.ArticleDTO;
import com.example.blogperformance.model.Article;
import com.example.blogperformance.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<ArticleDTO> getArticles() {
        List<Article> articles = articleService.getArticles();
        List<ArticleDTO> articleSummaries = new ArrayList<>();

        for (Article article : articles) {
            ArticleDTO summary = new ArticleDTO(article);
            articleSummaries.add(summary);
        }

        return articleSummaries;
    }

    @GetMapping("/{id}")
    public Article getArticle(@PathVariable Long id) {
        return articleService.getArticle(id);
    }

    @PostMapping
    public Article createArticle(@RequestBody Article article) {
        return articleService.createArticle(article);
    }

    @GetMapping("/similar/{articleId}")
    public List<Article> getSimilarArticles(@PathVariable Long articleId) {
        return articleService.findSimilarArticles(articleId);
    }
}