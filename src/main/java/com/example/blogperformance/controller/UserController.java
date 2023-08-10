package com.example.blogperformance.controller;

import com.example.blogperformance.dto.UserScoreDTO;
import com.example.blogperformance.model.Article;
import com.example.blogperformance.model.User;
import com.example.blogperformance.service.ArticleService;
import com.example.blogperformance.service.UserService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final ArticleService articleService;

    public UserController(UserService userService, ArticleService articleService) {
        this.userService = userService;
        this.articleService = articleService;
    }

    @GetMapping
    public List<User> getUsers() {
        return userService.getUsers();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Long id) {
        return userService.getUser(id);
    }

    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @GetMapping("/{id}/articles")
    public List<Article> getUserArticles(@PathVariable Long id) {
        return articleService.getArticlesByUserId(id);
    }

    @GetMapping("score")
    public List<UserScoreDTO> getUsersScore() {
        return userService.getUsersScore();
    }
}