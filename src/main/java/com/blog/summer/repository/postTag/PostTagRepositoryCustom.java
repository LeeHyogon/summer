package com.blog.summer.repository.postTag;

import com.blog.summer.domain.PostTag;

import java.util.Optional;

public interface PostTagRepositoryCustom {

    Optional<PostTag> findByPostAndTag(Long postId,Long tagId);
}
