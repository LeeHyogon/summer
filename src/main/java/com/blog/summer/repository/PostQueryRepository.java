package com.blog.summer.repository;


import com.blog.summer.domain.Post;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostQueryRepository {

    private final EntityManager em;

    public List<Post> findAllWithUserCommentFavorite(){
        return em.createQuery(
                        "select p from Post p" +
                                " join fetch p.postUser u" +
                                " join fetch p.comments c" +
                                " join fetch p.favorites f",Post.class)
                .getResultList();
    }


}
