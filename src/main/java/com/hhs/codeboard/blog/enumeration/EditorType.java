package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EditorType implements CodeboardEnum {

    TextArea("TA")
    , CodeEditor("CE")
    , HTMLEditor("HE") // WYSIWYG
    , MarkdownEditor("ME")
    ;

    private final String code;

}
