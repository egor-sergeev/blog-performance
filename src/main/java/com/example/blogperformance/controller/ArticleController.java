package com.example.blogperformance.controller;

import com.example.blogperformance.dto.ArticleSummaryDTO;
import com.example.blogperformance.dto.ArticleWithCommentsStatsDTO;
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
    public List<ArticleSummaryDTO> getArticles() {
        List<Article> articles = articleService.getArticles();
        List<ArticleSummaryDTO> articleSummaries = new ArrayList<>();

        for (Article article : articles) {
            ArticleSummaryDTO summary = new ArticleSummaryDTO(article.getId(), article.getTitle(), article.getPublicationDate());
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

    @GetMapping("/recent-comments")
    public List<ArticleWithCommentsStatsDTO> getArticlesWithRecentComments() {
        return articleService.getArticlesWithRecentComments();
    }

    @GetMapping("/similar/{articleId}")
    public List<Article> getSimilarArticles(@PathVariable Long articleId) {
        return articleService.findSimilarArticles(articleId);
    }
}