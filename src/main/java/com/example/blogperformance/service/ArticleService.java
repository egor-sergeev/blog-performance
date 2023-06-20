package com.example.blogperformance.service;

import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.Comment;
import com.example.blogperformance.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class ArticleService {

    private final ArticleRepository articleRepository;

    public ArticleService(ArticleRepository articleRepository) {
        this.articleRepository = articleRepository;
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
        // Simulating memory leak
        List<Article> articles = new ArrayList<>(articleRepository.findAll());
        return articles.stream()
                .filter(article -> article.getAuthor().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<Map<String, Object>> getArticlesWithRecentComments() {
        // Check cache

        List<Map<String, Object>> latestCommentedArticles = new ArrayList<>();

        while (latestCommentedArticles.size() < 20) {
            List<Article> articles = getRemainingArticles(latestCommentedArticles);
            Comment latestComment = getLatestCommentFromArticles(articles);

            if (latestComment != null) {
                Map<String, Object> articleSummaryMap = getArticleSummary(latestComment);
                latestCommentedArticles.add(articleSummaryMap);
            }
        }
        return latestCommentedArticles;
    }

    private Comment getLatestCommentFromArticles(List<Article> articles) {
        Comment latestComment = null;
        for (Article article : articles) {
            Comment comment = article.getLatestComment();
            if (comment != null && (latestComment == null || comment.getCreatedAt().isAfter(latestComment.getCreatedAt()))) {
                latestComment = comment;
            }
        }
        return latestComment;
    }

    private static Map<String, Object> getArticleSummary(Comment latestComment) {
        Map<String, Object> articleSummaryMap = new HashMap<>();
        Article article = latestComment.getArticle();

        articleSummaryMap.put("id", article.getId());
        articleSummaryMap.put("title", article.getTitle());
        articleSummaryMap.put("publicationDate", article.getPublicationDate());
        articleSummaryMap.put("numberOfComments", article.getComments().size());
        articleSummaryMap.put("mostRecentCommentDate", latestComment.getCreatedAt());

        return articleSummaryMap;
    }

    private List<Article> getRemainingArticles(List<Map<String, Object>> sortedArticleSummaries) {
        List<Article> articles = new ArrayList<>(articleRepository.findAll());

        // Remove already added articles from the list
        for (Map<String, Object> articleSummary : sortedArticleSummaries) {
            articles.removeIf(article -> article.getId().equals(articleSummary.get("id")));
        }
        return articles;
    }
}

