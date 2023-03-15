package com.hhs.codeboard.blog.enumeration;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.config.except.NotFoundDataException;
import lombok.RequiredArgsConstructor;

import java.util.function.Supplier;

@RequiredArgsConstructor
public enum ErrorType {

    NOT_FOUND_DATA("데이터를 찾을수 없습니다", NotFoundDataException::new);

    private final String message;
    private final Supplier<? extends CodeboardException> exceptionSupplier;

    public String getMessage() {
        return message;
    }

    public CodeboardException supply () {
        return exceptionSupplier.get();
    }
}
