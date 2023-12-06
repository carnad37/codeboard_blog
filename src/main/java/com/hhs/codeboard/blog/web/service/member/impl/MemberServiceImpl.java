package com.hhs.codeboard.blog.web.service.member.impl;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import com.hhs.codeboard.blog.web.service.member.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {

	private final WebClient memberClient;
	private final String authorizedHeaderEmailName = "X-USER-INFO";

	@Override
	public MemberDto getSelfInfo(String email) {

		CommonResponse<MemberDto> result = null;
		try {
			result = memberClient.get()
					.uri("/private/user/selfInfo")
					.header(authorizedHeaderEmailName, email)
					.retrieve()
					.bodyToMono(new ParameterizedTypeReference<CommonResponse<MemberDto>>() {})
					.block();
			if (result == null) {
				throw new NullPointerException();
			}
		} catch (RuntimeException re) {
			CodeboardException.error(ErrorType.API_ERROR, "회원");
		}
		return result.getData();
	}
}
