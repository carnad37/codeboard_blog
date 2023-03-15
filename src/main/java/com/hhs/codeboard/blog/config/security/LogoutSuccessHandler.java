package com.hhs.codeboard.blog.config.security;

import com.hhs.codeboard.blog.enumeration.UserSessionData;
import com.hhs.codeboard.blog.util.common.SessionUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserCache;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import java.util.Arrays;

public class LogoutSuccessHandler implements LogoutHandler {

    @Override
    public void logout(final HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Arrays.stream(UserSessionData.values()).forEach(
            sessionData -> {
                request.getSession().removeAttribute(sessionData.getValue());
            }
        );
    }

}
