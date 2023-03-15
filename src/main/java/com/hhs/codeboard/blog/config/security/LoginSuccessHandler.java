package com.hhs.codeboard.blog.config.security;

import com.hhs.codeboard.blog.web.service.member.MemberDto;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

import java.io.IOException;

public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Autowired
	MenuService menuService;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
										Authentication authentication) throws ServletException, IOException {
        
        MemberDto memberDto = (MemberDto) authentication.getPrincipal();
		//메뉴정보를 세션에 담는다
		menuService.initMenuList(memberDto, request);

		response.sendRedirect("/main");
	}
	
}
