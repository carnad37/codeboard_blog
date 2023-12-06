package com.hhs.codeboard.blog.config.except;

import com.hhs.codeboard.blog.enumeration.ErrorType;
import org.slf4j.Logger;

public class CodeboardException extends RuntimeException{

    public Object[] messages;

    public static CodeboardException error(ErrorType errorType) {
        throw errorType.supply();
    }

    public static CodeboardException error(ErrorType errorType, Object... parameter) {
        throw errorType.supply(parameter);
    }

    public CodeboardException() {
        super();
    }

    public CodeboardException(Object[] messages) {
        super();
        this.messages = messages;
    }

}
