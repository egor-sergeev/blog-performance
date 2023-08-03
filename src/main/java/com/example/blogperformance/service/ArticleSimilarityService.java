package com.example.blogperformance.service;

import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.Comment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleSimilarityService {
    double getTotalScore(Article potentialArticle, Article article) {
        double score = 1.0;

        score *= calculateTitleSimilarity(article, potentialArticle);
        score *= calculateContentSimilarity(article, potentialArticle);
        score *= calculateAuthorSimilarity(article, potentialArticle);
        score *= calculateRecentCommentsSimilarity(article, potentialArticle);
        score *= calculateCommentUserSimilarity(article, potentialArticle);
        return score;
    }

    private double jacquardSimilarity(String text1, String text2) {
        // Calculate Jacquard similarity between two texts
        Set<String> set1 = new HashSet<>(Arrays.asList(text1.split(" ")));
        Set<String> set2 = new HashSet<>(Arrays.asList(text2.split(" ")));

        Set<String> intersection = new HashSet<>(set1);
        intersection.retainAll(set2);

        Set<String> union = new HashSet<>(set1);
        union.addAll(set2);

        return (double) intersection.size() / union.size();
    }

    public double calculateTitleSimilarity(Article article1, Article article2) {
        return jacquardSimilarity(article1.getTitle(), article2.getTitle());
    }

    public double calculateContentSimilarity(Article article1, Article article2) {
        return jacquardSimilarity(article1.getContent(), article2.getContent());
    }

    public double calculateAuthorSimilarity(Article article1, Article article2) {
        return article1.getAuthor().getId().equals(article2.getAuthor().getId()) ? 1.0 : 0.0;
    }

    public double calculateRecentCommentsSimilarity(Article article1, Article article2) {
        Optional<Comment> latestComment1Opt = article1.getComments().stream().max(Comparator.comparing(Comment::getCreatedAt));
        Optional<Comment> latestComment2Opt = article2.getComments().stream().max(Comparator.comparing(Comment::getCreatedAt));

        // If either article has no comments, return 0
        if (latestComment1Opt.isEmpty() || latestComment2Opt.isEmpty()) {
            return 0;
        }

        LocalDateTime latestComment1 = latestComment1Opt.get().getCreatedAt();
        LocalDateTime latestComment2 = latestComment2Opt.get().getCreatedAt();
        return 1.0 / (1.0 + Math.abs(Duration.between(latestComment1, latestComment2).toDays()));
    }

    public double calculateCommentUserSimilarity(Article article1, Article article2) {
        double similarity = 0.0;
        int numberComments = 0;
        List<Comment> comments1 = article1.getComments();
        List<Comment> comments2 = article2.getComments();

        for (Comment comment1 : comments1) {
            for (Comment comment2 : comments2) {
                if (comment1.getUser().getId().equals(comment2.getUser().getId())) {
                    similarity += 1.0;
                    numberComments++;
                }
            }
        }
        similarity = similarity / numberComments;
        return similarity;
    }
}
