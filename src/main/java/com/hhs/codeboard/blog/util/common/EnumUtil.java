package com.hhs.codeboard.blog.util.common;

import com.hhs.codeboard.blog.enumeration.CodeboardEnum;
import lombok.experimental.UtilityClass;

import java.util.EnumSet;

/**
 * @see CodeboardEnum 의 구현체에서 원하는 값만 가져오는 Util
 * @see <a href="http://www.baeldung.com">https://techblog.woowahan.com/2600/</a>
 */
@UtilityClass
public class EnumUtil {

    public static <T extends Enum<T> & CodeboardEnum> T covertCodeboardEnum(Class<T> enumClass, String enumValue) {
        return EnumSet.allOf(enumClass).stream()
                .filter(target->target.getCode().equals(enumValue))
                .findFirst()
                .orElse(null);
    }

}
