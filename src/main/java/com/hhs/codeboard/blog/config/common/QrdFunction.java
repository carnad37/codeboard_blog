package com.hhs.codeboard.blog.config.common;

@FunctionalInterface
public interface QrdFunction<T,R,E,W,Q> {
    Q apply(T t, R r, E e, W w);
}
