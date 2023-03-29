package com.blog.summer.repository;

import com.blog.summer.domain.Member;
import com.blog.summer.domain.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    @Override
    Optional<Member> findById(Long aLong);

    @Override
    List<Member> findAll();

    List<Member> findByUsername(String username);

    Long save(Member member);

    @Override
    void delete(Member member);
}
