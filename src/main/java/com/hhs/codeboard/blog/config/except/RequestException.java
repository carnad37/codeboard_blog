package com.hhs.codeboard.blog.config.except;

import com.hhs.codeboard.blog.enumeration.CodeboardEnum;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import com.hhs.codeboard.blog.util.common.FormatUtil;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import java.util.Collection;

public class RequestException extends CodeboardException {

    public RequestException(Object[] messages) {
        super(messages);
    }

    public static void valid(String value, String parameterName) {
        if (!StringUtils.hasText(value)) CodeboardException.error(ErrorType.INVALID_PARAMETER, parameterName);
    }

    public static void valid(Collection<?> value, String parameterName) {
        if (CollectionUtils.isEmpty(value)) CodeboardException.error(ErrorType.INVALID_PARAMETER, parameterName);
    }

    public static void valid(Number value, Object... parameters) {
        if (!FormatUtil.Number.isPositive(value.longValue())) CodeboardException.error(ErrorType.INVALID_PARAMETER, parameters);
    }

    public static void valid(CodeboardEnum value, Object... parameters) {
        if (value == null) CodeboardException.error(ErrorType.INVALID_PARAMETER, parameters);
    }
}
