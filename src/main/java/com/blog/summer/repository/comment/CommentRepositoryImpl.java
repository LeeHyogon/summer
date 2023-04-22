package com.blog.summer.repository.comment;


import com.blog.summer.domain.QComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

import static com.blog.summer.domain.QComment.comment;

@RequiredArgsConstructor
public class CommentRepositoryImpl implements CommentRepositoryCustom{

    private final JPAQueryFactory queryFactory;
    @Override
    @Transactional
    public void deleteCommentsByPostId(Long postId) {
        /*
        Query query = em.createNativeQuery("DELETE FROM comment WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
         */
        queryFactory
                .delete(comment)
                .where(comment.commentPost.id.eq(postId))
                .execute();
    }
}
