package com.example.blogperformance.repository;

import com.example.blogperformance.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
