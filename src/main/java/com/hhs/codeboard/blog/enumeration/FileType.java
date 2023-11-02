package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileType implements CodeboardEnum {

    ARTICLE_CONTENT("AC");

    private final String code;
}
