package com.hhs.codeboard.blog.enumeration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@RequiredArgsConstructor
@Getter
public enum MenuTypeEnum implements CodeboardEnum {
    
    BOARD("B", (uuid) -> "/board/" + uuid + "/list")
    , MENU("M", (uuid) -> null)
    , BOARD_CONFIG("D", (func) -> "/board/" + func)
    , MENU_CONFIG("U", (func) -> "/menu/" + func)
    , COMMON_BOARD("C", (uuid) -> "/common/board")
    , STATIC_MENU("S", (uuid) -> null)
    , CATEGORY_CONFIG("Y", (uuid) -> "/category/config");

    private final String code;
    private final Function<String, String> url;

//    private final static Map<String, MenuTypeEnum> innerTypeMap = Arrays.stream(MenuTypeEnum.values()).collect(Collectors.toUnmodifiableMap(MenuTypeEnum::getDbValue, Function.identity()));
//
//    public static MenuTypeEnum getEnumByMenuType(String menuType) {
//        return innerTypeMap.getOrDefault(menuType, null);
//    }

}
