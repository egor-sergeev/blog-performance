package com.example.blogperformance.service;

import com.example.blogperformance.dto.ArticleWithCommentsStatsDTO;
import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.Comment;
import com.example.blogperformance.repository.ArticleRepository;
import org.springframework.stereotype.Service;

import java.util.*;
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
        // Simulating memory leak
        List<Article> articles = new ArrayList<>(articleRepository.findAll());
        return articles.stream()
                .filter(article -> article.getAuthor().getId().equals(userId))
                .collect(Collectors.toList());
    }

    public List<ArticleWithCommentsStatsDTO> getArticlesWithRecentComments() {
        // Check cache
        List<ArticleWithCommentsStatsDTO> latestCommentedArticles = new ArrayList<>();

        while (latestCommentedArticles.size() < 20) {
            List<Article> articles = getRemainingArticles(latestCommentedArticles);
            Comment latestComment = getLatestCommentFromArticles(articles);

            if (latestComment != null) {
                Article article = latestComment.getArticle();
                ArticleWithCommentsStatsDTO articleSummary = new ArticleWithCommentsStatsDTO(article.getId(),
                        article.getTitle(),
                        article.getPublicationDate(),
                        article.getComments().size(),
                        latestComment.getCreatedAt());
                latestCommentedArticles.add(articleSummary);
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

    private List<Article> getRemainingArticles(List<ArticleWithCommentsStatsDTO> sortedArticleSummaries) {
        List<Article> articles = new ArrayList<>(articleRepository.findAll());

        // Remove already added articles from the list
        for (ArticleWithCommentsStatsDTO articleSummary : sortedArticleSummaries) {
            articles.removeIf(article -> article.getId().equals(articleSummary.getId()));
        }
        return articles;
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


    private static List<Article> getTopMatchingArticles(Map<Article, Double> similarityScores) {
        // Sorting the map based on values (similarity scores)
        List<Map.Entry<Article, Double>> entryList = new ArrayList<>(similarityScores.entrySet());
        entryList.sort(Map.Entry.comparingByValue(Comparator.reverseOrder()));

        // Getting the top 3 articles
        List<Article> topArticles = new ArrayList<>();
        for (int i = 0; i < 3 && i < entryList.size(); i++) {
            topArticles.add(entryList.get(i).getKey());
        }
        return topArticles;
    }
}

