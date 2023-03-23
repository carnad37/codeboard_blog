package com.hhs.codeboard.blog.data.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hhs.codeboard.blog.data.entity.member.entity.MemberEntity;

public interface MemberDAO extends JpaRepository<MemberEntity, Long> {
	Optional<MemberEntity> findByEmail(String email);
}
