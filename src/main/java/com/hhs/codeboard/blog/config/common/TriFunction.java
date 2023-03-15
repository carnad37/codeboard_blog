package com.hhs.codeboard.blog.config.common;

@FunctionalInterface
public interface TriFunction<T,R,E,W> {
    W apply(T t, R r, E e);
}
