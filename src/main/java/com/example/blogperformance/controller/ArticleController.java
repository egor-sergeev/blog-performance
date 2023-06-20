package com.example.blogperformance.controller;

import com.example.blogperformance.model.Article;
import com.example.blogperformance.service.ArticleService;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/articles")
public class ArticleController {

    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @GetMapping
    public List<Map<String, Object>> getArticles() {
        List<Article> articles = articleService.getArticles();
        List<Map<String, Object>> articleSummaries = new ArrayList<>();

        for (Article article : articles) {
            Map<String, Object> summary = new HashMap<>();
            summary.put("id", article.getId());
            summary.put("title", article.getTitle());
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
    public List<Map<String, Object>> getArticlesWithRecentComments() {
        return articleService.getArticlesWithRecentComments();
    }
}