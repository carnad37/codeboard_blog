package com.hhs.codeboard.blog.web.service.member;

import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;

public interface MemberService {

	MemberDto getSelfInfo(String email);
}
