package com.example.blogperformance.repository;

import com.example.blogperformance.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    Integer countByArticleId(Long articleId);
    Integer countByUserId(Long userId);
}
