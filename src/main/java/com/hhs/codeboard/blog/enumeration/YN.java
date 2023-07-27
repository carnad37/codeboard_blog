package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YN {

    Y(true)
    , N(false);

    private final boolean value;

}
