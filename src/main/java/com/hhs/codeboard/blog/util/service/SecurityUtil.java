package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import lombok.experimental.UtilityClass;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@UtilityClass
public class SecurityUtil {

    public static boolean isLogin(MemberDto memberDto) {
        return checkPrinciple(memberDto);
    }

    private static boolean checkPrinciple(MemberDto memberDto) {
        return memberDto != null && memberDto.getUserSeq() > 0;
    }

    private static Authentication getSecurityContextHolder() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

//    public boolean isLogin() {
//        return getSecurityContextHolder().isAuthenticated();
//    }

    public static boolean isLogin() {
        return getSecurityContextHolder().isAuthenticated() && !"anonymousUser".equals(getSecurityContextHolder().getPrincipal());
    }

    public static MemberDto getUser() {
        return Optional.ofNullable(getSecurityContextHolder())
                .filter(target->isLogin())
                .map(target->(MemberDto) target.getPrincipal())
                .orElseThrow(()-> CodeboardException.error(ErrorType.NOT_LOGIN));
    }

    public static Long getUserSeq() {
        return Optional.ofNullable(getSecurityContextHolder())
                    .filter(target->isLogin())
                    .map(target->(MemberDto) target.getPrincipal())
                    .map(MemberDto::getUserSeq)
                    .orElseThrow(()-> CodeboardException.error(ErrorType.NOT_LOGIN));
    }

    public static String getEmail() {
        return ((MemberDto) getSecurityContextHolder().getPrincipal()).getEmail();
    }

}
