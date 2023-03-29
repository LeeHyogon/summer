package com.blog.summer.repository;


import com.blog.summer.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post,Long> {

    @Override
    List<Post> findAll();

    @Override
    List<Post> findAllById(Iterable<Long> longs);

    @Override
    Optional<Post> findById(Long id);
    Long save(Post post);


    void delete(Post post);
}
