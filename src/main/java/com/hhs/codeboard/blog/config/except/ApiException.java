package com.hhs.codeboard.blog.config.except;

public class ApiException extends CodeboardException{

    public ApiException(Object[] messages) {
        super(messages);
    }
}
