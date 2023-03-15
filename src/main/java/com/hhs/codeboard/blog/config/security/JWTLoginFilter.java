package com.hhs.codeboard.blog.config.security;

import com.hhs.codeboard.blog.jpa.entity.member.dto.MemberDto;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;

@RequiredArgsConstructor
public class JWTLoginFilter extends OncePerRequestFilter {

    private final WebClient webClient;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String userToken = request.getParameter("token");
        String userSeq = request.getParameter("userSeq");
        // 유저정보 얻기
        String memberUrl = "http://localhost:20000/member/public/login&token=%s&userSeq=%s".formatted(userToken, userSeq);
        MemberDto memberInfo = webClient.get()
                .uri(memberUrl)
                .retrieve()
                .bodyToMono(MemberDto.class)
                .block();
        UsernamePasswordAuthenticationToken user = new UsernamePasswordAuthenticationToken(memberInfo, userToken);
        SecurityContextHolder.getContext().setAuthentication(user);

    }


}
