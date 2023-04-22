package com.blog.summer.repository.comment;


import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{
    private final EntityManager em;
    @Override
    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        Query query = em.createNativeQuery("DELETE FROM comment WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
    }
}
