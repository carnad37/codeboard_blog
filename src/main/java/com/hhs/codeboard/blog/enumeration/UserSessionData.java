package com.hhs.codeboard.blog.enumeration;

public enum UserSessionData {

    MENU_LIST("menuList")
    , MENU_MAP("menuMap")
    , MEX_DEPTH("maxDepth");

    private String value;

    UserSessionData(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

}
