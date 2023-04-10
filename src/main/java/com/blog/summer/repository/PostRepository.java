package com.blog.summer.repository;

import com.blog.summer.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {


    @Query("SELECT p FROM Post p JOIN FETCH p.postUser WHERE p.id = :id")
    Optional<Post> findByIdWithUser(@Param("id") Long id);
    @Query("SELECT p FROM Post p JOIN FETCH p.postUser Join FETCH p.comments WHERE p.id = :id")
    Optional<Post> findByIdWithUserComment(@Param("id") Long id);

    @Query("SELECT p FROM Post p JOIN FETCH p.postUser")
    List<Post> findAllWithUser();

}
