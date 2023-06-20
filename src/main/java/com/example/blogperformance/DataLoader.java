package com.example.blogperformance;


import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.Comment;
import com.example.blogperformance.model.User;
import com.example.blogperformance.repository.ArticleRepository;
import com.example.blogperformance.repository.CommentRepository;
import com.example.blogperformance.repository.UserRepository;
import com.github.javafaker.Faker;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class DataLoader implements CommandLineRunner {
    public static final int NUM_USERS = 1000;
    public static final int NUM_ARTICLES = 50_000;
    public static final int NUM_COMMENTS = 200_000;
    private final UserRepository userRepository;
    private final ArticleRepository articleRepository;
    private final CommentRepository commentRepository;

    private static final Logger log = LoggerFactory.getLogger(DataLoader.class);

    public DataLoader(UserRepository userRepository, ArticleRepository articleRepository, CommentRepository commentRepository) {
        this.userRepository = userRepository;
        this.articleRepository = articleRepository;
        this.commentRepository = commentRepository;
    }

    @Override
    @Transactional
    public void run(String... args) {
        if (userRepository.count() != NUM_USERS ||
                articleRepository.count() != NUM_ARTICLES ||
                commentRepository.count() != NUM_COMMENTS) {
            log.info("Generating sample data...");
            commentRepository.deleteAll();
            articleRepository.deleteAll();
            userRepository.deleteAll();
            loadData();
            log.info("Sample data generation completed.");
        } else log.info("Sample data already generated");
    }

    private void loadData() {
        Faker faker = new Faker();
        Random random = new Random();

        generateUsers(faker);
        generateArticles(faker, random);
        generateComments(faker, random);
    }

    private void generateUsers(Faker faker) {
        List<User> users = new ArrayList<>();
        for (int i = 0; i < NUM_USERS; i++) {
            User user = new User();
            user.setName(faker.name().fullName());
            user.setEmail(faker.internet().emailAddress());
            users.add(user);
        }
        userRepository.saveAll(users);
    }

    private void generateArticles(Faker faker, Random random) {
        List<User> users = userRepository.findAll();
        List<Article> articles = new ArrayList<>();
        for (int i = 0; i < NUM_ARTICLES; i++) {
            Article article = new Article();
            article.setTitle(faker.book().title());
            article.setContent(faker.lorem().paragraph());
            article.setPublicationDate(faker.date().past(365, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            // Assign a random user as the author of the article
            article.setAuthor(users.get(random.nextInt(users.size())));
            articles.add(article);
        }
        articleRepository.saveAll(articles);
    }

    private void generateComments(Faker faker, Random random) {
        List<Comment> comments = new ArrayList<>();
        List<User> users = userRepository.findAll();
        List<Article> articles = articleRepository.findAll();
        for (int i = 0; i < NUM_COMMENTS; i++) {
            Comment comment = new Comment();
            comment.setContent(faker.lorem().sentence());
            comment.setCreatedAt(faker.date().past(30, TimeUnit.DAYS).toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime());
            // Assign a random user as the author of the comment
            comment.setUser(users.get(random.nextInt(users.size())));
            // Assign a random article for the comment
            comment.setArticle(articles.get(random.nextInt(articles.size())));
            comments.add(comment);
        }
        commentRepository.saveAll(comments);
    }
}