package com.hhs.codeboard.blog.web.service.member;

import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface MemberService {

	MemberDto authorized(String token);
}
