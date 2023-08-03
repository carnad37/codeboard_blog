package com.hhs.codeboard.blog.util.common;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDto;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@UtilityClass
public class FormatUtil {

    @UtilityClass
    public static class Number {

        public static boolean isPositive(Long number) {
            return number != null && number > 0;
        }

        public static boolean isPositive(Double number) {
            return number != null && number > 0;
        }

        public static boolean isPositive(Integer number) {
            return number != null && number > 0;
        }

    }

    public static String formatDate(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

}
