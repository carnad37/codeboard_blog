package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public enum FileType implements CodeboardEnum {

    ARTICLE_CONTENT("AC", "article_content");

    private final String code, addName;
}
