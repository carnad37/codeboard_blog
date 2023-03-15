package com.hhs.codeboard.blog.web.service.member;

import org.springframework.security.core.userdetails.UserDetailsService;

import com.hhs.codeboard.blog.jpa.entity.member.entity.MemberEntity;

public interface MemberService extends UserDetailsService {
	String insertUser(MemberEntity memberVO);
}
