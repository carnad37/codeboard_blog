package com.hhs.codeboard.blog.config.anno;

import java.lang.annotation.*;

@Documented
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface TreeMappingKey {

    String rootValue() default "0";

}
