package com.hhs.codeboard.blog.web.service.member.impl;

import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.web.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final WebClient memberClient;

	@Override
	public MemberDto authorized(String token) {
		return memberClient.get()
				.uri("/public/login&token=%s".formatted(token))
				.retrieve()
				.bodyToMono(com.hhs.codeboard.blog.data.entity.member.dto.MemberDto.class)
				.block();
	}
}
