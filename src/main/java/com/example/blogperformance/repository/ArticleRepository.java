package com.example.blogperformance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blogperformance.model.Article;

public interface ArticleRepository extends JpaRepository<Article, Long> {
}
