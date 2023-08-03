package com.hhs.codeboard.blog.config.except;

public class NotFoundDataException extends CodeboardException {

    public NotFoundDataException(String message) {
        super(message);
    }

    public NotFoundDataException() {
        super(null);
    }
}
