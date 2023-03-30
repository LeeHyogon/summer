package com.blog.summer.repository;

import com.blog.summer.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member,Long> {
    Member findByUsername(String username);
}
