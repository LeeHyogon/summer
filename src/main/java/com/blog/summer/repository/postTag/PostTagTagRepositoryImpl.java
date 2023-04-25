package com.blog.summer.repository.postTag;

import com.blog.summer.domain.PostTag;
import com.blog.summer.domain.QPost;
import com.blog.summer.domain.QPostTag;
import com.blog.summer.domain.QTag;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static com.blog.summer.domain.QPost.post;
import static com.blog.summer.domain.QPostTag.*;
import static com.blog.summer.domain.QTag.tag;

@RequiredArgsConstructor
public class PostTagTagRepositoryImpl implements PostTagRepositoryCustom {

    private final JPAQueryFactory queryFactory;
    @Override
    public Optional<PostTag> findByPostIdAndTagId(Long postId, Long tagId) {
        return Optional.ofNullable(
                queryFactory
                        .select(postTag)
                        .from(postTag)
                        .join(postTag.tag, tag).fetchJoin()
                        .join(postTag.tagPost, post).fetchJoin()
                        .where(
                                postTag.tagPost.id.eq(postId),
                                postTag.tag.id.eq(tagId)
                        ).fetchOne()
        );
    }
}
