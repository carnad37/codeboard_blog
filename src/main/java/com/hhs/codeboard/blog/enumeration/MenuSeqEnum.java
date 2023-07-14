package com.hhs.codeboard.blog.enumeration;

public enum MenuSeqEnum {

    STATIC_MENU(-1), ROOT_MENU(0);

    private long menuSeq;

    MenuSeqEnum (long menuSeq) {
        this.menuSeq = menuSeq;
    }

    public long getMenuSeq() {
        return this.menuSeq;
    }
    
}
