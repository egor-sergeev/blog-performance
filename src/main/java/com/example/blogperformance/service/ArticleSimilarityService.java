package com.example.blogperformance.service;

import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.Comment;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class ArticleSimilarityService {
    public double getTotalScore(Article potentialArticle, Article article) {
        double score = 1.0;

        score *= calculateTitleSimilarity(article, potentialArticle);
        score *= calculateContentSimilarity(article, potentialArticle);
        score *= calculateAuthorSimilarity(article, potentialArticle);
        score *= calculateRecentCommentsSimilarity(article, potentialArticle);
        score *= calculateCommentUserSimilarity(article, potentialArticle);
        return score;
    }

    public double calculateTitleSimilarity(Article article1, Article article2) {
        return getTextSimilarity(article1.getTitle(), article2.getTitle());
    }

    public double calculateContentSimilarity(Article article1, Article article2) {
        return getTextSimilarity(article1.getContent(), article2.getContent());
    }

    public double calculateAuthorSimilarity(Article article1, Article article2) {
        return article1.getAuthor().getId().equals(article2.getAuthor().getId()) ? 1.0 : 0.0;
    }

    public double calculateRecentCommentsSimilarity(Article article1, Article article2) {
        Comment latestComment1 = findLatestComment(article1);
        Comment latestComment2 = findLatestComment(article2);

        if (latestComment1 == null || latestComment2 == null) {
            return 0;
        }

        return calculateSimilarity(latestComment1, latestComment2);
    }

    private double getTextSimilarity(String text1, String text2) {
        return jacquardSimilarity(text1, text2);
    }

    private double jacquardSimilarity(String text1, String text2) {
        Set<String> intersectionSet = getIntersection(text1, text2);
        Set<String> unionSet = getUnionSet(text1, text2);
        return (double) intersectionSet.size() / (double) unionSet.size();
    }

    private static Set<String> getUnionSet(String text1, String text2) {
        List<String> words1 = Arrays.asList(text1.split(" "));
        List<String> words2 = Arrays.asList(text2.split(" "));
        List<String> union = new ArrayList<>(words1);
        union.addAll(words2);
        return new HashSet<>(union);
    }

    private static Set<String> getIntersection(String text1, String text2) {
        List<String> words1 = Arrays.asList(text1.split(" "));
        List<String> words2 = Arrays.asList(text2.split(" "));
        List<String> intersection = new ArrayList<>(words1);
        intersection.retainAll(words2);
        return new HashSet<>(intersection);
    }

    private Comment findLatestComment(Article article) {
        return article.getComments().stream()
                .max(Comparator.comparing(Comment::getCreatedAt))
                .orElse(null);
    }

    private double calculateSimilarity(Comment comment1, Comment comment2) {
        LocalDateTime dateTime1 = comment1.getCreatedAt();
        LocalDateTime dateTime2 = comment2.getCreatedAt();
        return 1.0 / (1.0 + Math.abs(Duration.between(dateTime1, dateTime2).toDays()));
    }


    public double calculateCommentUserSimilarity(Article article1, Article article2) {
        double similarity = 0.0;
        int commentCount = 0;
        List<Comment> comments1 = article1.getComments();
        List<Comment> comments2 = article2.getComments();

        for (Comment comment1 : comments1) {
            for (Comment comment2 : comments2) {
                if (comment1.getUser().getId().equals(comment2.getUser().getId())) {
                    similarity += 1.0;
                    commentCount++;
                }
            }
        }

        similarity = similarity / commentCount;
        return similarity;
    }
}
