package com.example.app.repository;

import com.example.app.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserName(String userName);
    User findByEmail(String email);
    User findByNumber(String number);
    User findById(int id);
    boolean existsByUserName(String userName);
    boolean existsByEmail(String email);
    boolean existsByNumber(String number);
}
