package com.blog.summer.repository;

import com.blog.summer.domain.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    UserEntity findByUserId(String userId);

    UserEntity findByEmail(String username);

    UserEntity findByName(String name);
}
