package com.example.blogperformance.repository;

import com.example.blogperformance.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> findByArticleId(Long articleId);

    List<Comment> findByUserId(Long userId);

    Integer countByArticleId(Long articleId);

    Integer countByUserId(Long userId);
}
