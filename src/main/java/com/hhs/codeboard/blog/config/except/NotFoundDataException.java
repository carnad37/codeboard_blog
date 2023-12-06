package com.hhs.codeboard.blog.config.except;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

public class NotFoundDataException extends CodeboardException {

    public NotFoundDataException(Object[] messages) {
        super(messages);
    }
}
