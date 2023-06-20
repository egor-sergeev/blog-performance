package com.example.blogperformance.service;

import org.springframework.stereotype.Service;
import com.example.blogperformance.model.Comment;
import com.example.blogperformance.repository.CommentRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CommentService {

    private final CommentRepository commentRepository;

    public CommentService(CommentRepository commentRepository) {
        this.commentRepository = commentRepository;
    }

    public List<Comment> getComments() {
        return commentRepository.findAll();
    }

    public Comment getComment(Long id) {
        return commentRepository.findById(id).orElse(null);
    }

    public Comment createComment(Comment comment) {
        return commentRepository.save(comment);
    }

    public List<Comment> getCommentsByArticleId(Long articleId) {
        return commentRepository.findAll().stream()
                .filter(comment -> comment.getArticle().getId().equals(articleId))
                .collect(Collectors.toList());
    }
}
