package com.example.blogperformance.service;

import com.example.blogperformance.dto.UserScoreDTO;
import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.User;
import com.example.blogperformance.repository.ArticleRepository;
import com.example.blogperformance.repository.CommentRepository;
import com.example.blogperformance.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    private final Map<Date, List<UserScoreDTO>> userScoreCache = new HashMap<>();

    private static final Logger log = LoggerFactory.getLogger(UserService.class);


    public UserService(UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    public List<User> getUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long id) {
        return userRepository.findById(id).orElse(null);
    }

    public User createUser(User user) {
        return userRepository.save(user);
    }

    public List<UserScoreDTO> getUsersScore() {
        Date today = Date.from(LocalDate.now().atStartOfDay(ZoneId.systemDefault()).toInstant());

        if (userScoreCache.containsKey(today)) {
            return userScoreCache.get(today);
        }

        List<User> users = userRepository.findAll();
        List<UserScoreDTO> stats = new ArrayList<>();

        for (User user : users) {
            List<Article> userArticles = articleRepository.findByAuthorId(user.getId());
            int commentsCount = getCommentsCount(user);
            int articlesCount = userArticles.size();
            int articlesCommentsCount = getArticlesCommentsCount(userArticles);
            int score = calculateUserScore(commentsCount, articlesCount, articlesCommentsCount);

            stats.add(new UserScoreDTO(user.getId(), user.getName(), score, userArticles));
        }

        stats.sort(Comparator.comparingInt(UserScoreDTO::getScore).reversed());
        userScoreCache.put(today, stats);

        today.setTime(LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli());
        log.info("Users score calculated at {}", today);
        return stats;
    }

    private Integer getCommentsCount(User user) {
        return commentRepository.countByUserId(user.getId());
    }

    private int getArticlesCommentsCount(List<Article> userArticles) {
        int commentCount = 0;
        for (Article article : userArticles) {
            commentCount += commentRepository.countByArticleId(article.getId());
        }
        return commentCount;
    }

    private int calculateUserScore(int commentsCount, int articlesCount, int numCommentsPerOwnedArticle) {
        return commentsCount + 10 * articlesCount + numCommentsPerOwnedArticle;
    }
}
