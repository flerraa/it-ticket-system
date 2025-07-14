package com.support.ticketing.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.support.ticketing.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}
