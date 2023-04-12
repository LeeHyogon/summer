package com.blog.summer.repository;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class CommentQueryRepository {
    private final EntityManager em;

    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        Query query = em.createNativeQuery("DELETE FROM comment WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
    }
}
