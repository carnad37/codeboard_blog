package com.hhs.codeboard.blog.util.common;

import jakarta.servlet.http.HttpServletRequest;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

@UtilityClass
public class SessionUtil {

    public static void setSession (HttpServletRequest request, String key, Object value) {
        request.getSession().setAttribute(key, value);
    }

    public static <T> T getSession (HttpServletRequest request, String key) {
        return (T) request.getSession().getAttribute(key);
    }

}
