package com.hhs.codeboard.blog.util.service;

import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

@Component
public class MenuUtil {

    public static boolean checkMenuActive(String menuType, String viewMenuType, String uuid, String viewMenuUUID) {
        if (StringUtils.hasText(uuid) && StringUtils.hasText(viewMenuUUID)) {
            //실제 게시판등 기능 메뉴일경우
            return viewMenuUUID.equals(uuid);
        } else {
            //해당값이 null일 경우 메뉴타입만 체크하면 된다.
            return menuType.equals(viewMenuType);
        }
    }
}
