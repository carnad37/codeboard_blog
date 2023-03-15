package com.hhs.codeboard.blog.config.security;


import com.hhs.codeboard.blog.web.service.member.MemberDto;
import com.hhs.codeboard.blog.web.service.member.MemberService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;

public class AuthenticationProviderImpl implements AuthenticationProvider {

    @Autowired
    MemberService memberService;

    @Autowired
    PasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String userEmail = (String) authentication.getPrincipal();
        String userPasswd = (String) authentication.getCredentials();

        MemberDto userVO = (MemberDto) memberService.loadUserByUsername(userEmail);

        if (!passwordEncoder.matches(userPasswd, userVO.getPassword())) {
            // throw new BadCredentialsException("잘못된 로그인 정보입니다.");
            throw new BadCredentialsException("잘못된 로그인 정보입니다.");
        }

        return new UsernamePasswordAuthenticationToken(userVO, userPasswd, userVO.getAuthorities());
	}
	
	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}
