package com.hhs.codeboard.blog.enumeration;

import com.hhs.codeboard.blog.web.service.menu.MenuVO;

import java.util.function.Function;

public enum MenuTypeEnum {
    
    BOARD("B", (uuid) -> "/board/" + uuid + "/list")
    , MENU("M", (uuid) -> null)
    , BOARD_CONFIG("D", (func) -> "/board/" + func)
    , MENU_CONFIG("U", (func) -> "/menu/" + func)
    , COMMON_BOARD("C", (uuid) -> "/common/board")
    , STATIC_MENU("S", (uuid) -> null)
    , CATEGORY_CONFIG("Y", (uuid) -> "/category/config");

    private String menuType;
    private Function<String, String> url;

    MenuTypeEnum (String menuType, Function<String, String> url) {
        this.menuType = menuType;
        this.url = url;
    }

    public String getMenuType() {
        return this.menuType;
    }

    public String getUrl(String value) {
        return url.apply(value);
    }

    public String getUrl(MenuVO entity) {
        if (this.equals(MenuTypeEnum.MENU) || this.equals(MenuTypeEnum.BOARD)) {
            //메뉴랑 게시판은 uuid로 해당 메뉴를 보낸다.
            return url.apply(entity.getUuid());
        } else {
            //메뉴랑 게시판이 아니면 list로 돌린다
            return url.apply("list");
        }
    }

    public static MenuTypeEnum getEnumByMenuType(String menuType) {
        for (MenuTypeEnum targetEnum : values()) {
            if (targetEnum.getMenuType().equals(menuType)) return targetEnum;
        }
        return null;
    }

}
