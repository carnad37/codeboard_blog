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
	private final String authorizedHeaderEmailName = "X-USER-INFO";

	@Override
	public MemberDto getSelfInfo(String email) {

		return memberClient.get()
				.uri("/private/user/selfInfo")
				.header(authorizedHeaderEmailName, email)
				.retrieve()
				.bodyToMono(MemberDto.class)
				.block();
	}
}
