package com.hhs.codeboard.blog.config.anno;

import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;

import java.lang.annotation.*;

@Documented
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
public @interface AspectMenuActive {
    MenuTypeEnum menuType() default MenuTypeEnum.MENU;
    String menuTitle() default "";
}
