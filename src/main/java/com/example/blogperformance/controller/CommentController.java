package com.example.blogperformance.controller;

import com.example.blogperformance.model.Comment;
import com.example.blogperformance.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comments")
public class CommentController {

    @Autowired
    private CommentService commentService;

    @GetMapping
    public List<Comment> getComments() {
        return commentService.getComments();
    }

    @GetMapping("/{id}")
    public Comment getComment(@PathVariable Long id) {
        return commentService.getComment(id);
    }

    @PostMapping
    public Comment createComment(@RequestBody Comment comment) {
        return commentService.createComment(comment);
    }

    @GetMapping("/articles/{id}")
    public List<Comment> getCommentsByArticleId(@PathVariable Long id) {
        return commentService.getCommentsByArticleId(id);
    }
}