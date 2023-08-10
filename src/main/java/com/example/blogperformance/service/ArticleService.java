package com.example.blogperformance.service;

import com.example.blogperformance.model.Article;
import com.example.blogperformance.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final ArticleSimilarityService articleSimilarityService;

    public ArticleService(ArticleRepository articleRepository, ArticleSimilarityService articleSimilarityService) {
        this.articleRepository = articleRepository;
        this.articleSimilarityService = articleSimilarityService;
    }

    public List<Article> getArticles() {
        return articleRepository.findAll();
    }

    public Article getArticle(Long id) {
        return articleRepository.findById(id).orElse(null);
    }

    public Article createArticle(Article article) {
        return articleRepository.save(article);
    }

    public List<Article> getArticlesByUserId(Long userId) {
        return articleRepository.findAll().stream()
                .filter(article -> article.getAuthor().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Article> findSimilarArticles(Long articleId) {
        Article article = articleRepository.findById(articleId).orElseThrow();

        Map<Article, Double> similarityScores = new HashMap<>();

        List<Article> allArticles = articleRepository.findAll();
        for (Article potentialArticle : allArticles) {
            if (!potentialArticle.getId().equals(articleId)) {
                double score = articleSimilarityService.getTotalScore(potentialArticle, article);
                similarityScores.put(potentialArticle, score);
            }
        }

        return getTopMatchingArticles(similarityScores);
    }

    private List<Article> getTopMatchingArticles(Map<Article, Double> similarityScores) {
        // Sorting the map based on similarity scores and limiting to top 3
        return similarityScores.entrySet().stream()
                .sorted(Map.Entry.<Article, Double>comparingByValue().reversed())
                .limit(3)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
    }
}
