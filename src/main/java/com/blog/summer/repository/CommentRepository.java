package com.blog.summer.repository;

import com.blog.summer.domain.Comment;
import com.blog.summer.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface CommentRepository extends JpaRepository<Comment,Long> {

    List<Comment> findByCommentPost(Post post);
}
