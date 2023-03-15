package com.hhs.codeboard.blog.enumeration;

public enum MenuSeqEnum {

    STATIC_MENU(-1), ROOT_MENU(0);

    private int menuSeq;

    MenuSeqEnum (int menuSeq) {
        this.menuSeq = menuSeq;
    }

    public int getMenuSeq() {
        return this.menuSeq;
    }
    
}
