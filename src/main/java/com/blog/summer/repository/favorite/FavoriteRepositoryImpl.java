package com.blog.summer.repository.favorite;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import javax.transaction.Transactional;

@Repository
@RequiredArgsConstructor
public class FavoriteRepositoryImpl implements FavoriteRepositoryCustom{

    private final EntityManager em;
    @Transactional
    @Override
    public void deleteFavoritesByPostId(Long postId) {
        Query query = em.createNativeQuery("DELETE FROM favorite WHERE post_id = :postId");
        query.setParameter("postId", postId);
        query.executeUpdate();
    }
}
