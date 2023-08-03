package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum YN {

    Y("Y", true)
    , N("N", false);

    private final String code;
    private final boolean value;

}
