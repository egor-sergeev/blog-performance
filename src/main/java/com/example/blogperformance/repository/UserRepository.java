package com.example.blogperformance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.example.blogperformance.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
