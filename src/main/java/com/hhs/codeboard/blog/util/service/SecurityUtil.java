package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    private Authentication getSecurityContextHolder() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

//    public boolean isLogin() {
//        return getSecurityContextHolder().isAuthenticated();
//    }

    public boolean isLogin() {
        return getSecurityContextHolder() != null;
    }

    public MemberDto getUser() {
        return (MemberDto) getSecurityContextHolder().getPrincipal();
    }

    public int getUserSeq() {
        return this.isLogin() ? ((MemberDto) getSecurityContextHolder().getPrincipal()).getUserSeq() : -1;
    }

    public String getEmail() {
        return ((MemberDto) getSecurityContextHolder().getPrincipal()).getEmail();
    }

}
