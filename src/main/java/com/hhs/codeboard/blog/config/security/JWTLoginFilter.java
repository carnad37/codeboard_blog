package com.hhs.codeboard.blog.config.security;

import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.web.service.member.MemberService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends OncePerRequestFilter {

    private final MemberService memberService;

    private final String authorizedHeaderEmailName = "X-USER-INFO";
    private final String authorizedHeaderSeqName = "X-USER-SEQ";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        /**
         *  TODO :: 차후에 헤더에 파라미터로 인증 타입정보 같이 넘겨주고, 타입에 맞는 인증과정 필요
         *  공통 라이브러리로 만들면 편해보임
         *  현재는 Email식으로 고정
         */
        String email = request.getHeader(authorizedHeaderEmailName);
        String userSeq = request.getHeader(authorizedHeaderSeqName);

        if (StringUtils.hasText(email)) {
            // 유저정보 얻기
            try {
                MemberDto memberInfo = memberService.authorized(email);
                MemberDto memberDto = new MemberDto();
                memberDto.setEmail(email);
                memberDto.setUserSeq(Long.parseLong(userSeq));
                UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(memberDto, null);
                SecurityContextHolder.getContext().setAuthentication(user);
            } catch (NumberFormatException ne) {
                // 이상데이터는 그냥 넘겨버림
            }
        }
        filterChain.doFilter(request, response);
    }


}
