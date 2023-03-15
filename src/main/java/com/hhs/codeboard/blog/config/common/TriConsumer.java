package com.hhs.codeboard.blog.config.common;

@FunctionalInterface
public interface TriConsumer<T,R,E> {
    void accept(T t, R r, E e);
}
