package com.blog.summer.repository;

import com.blog.summer.domain.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    @Override
    List<Comment> findAll();

    List<Comment> findAllById(Long id);

    @Override
    Optional<Comment> findById(Long id);
    Long save(Comment comment);

    void delete(Comment comment);
}
