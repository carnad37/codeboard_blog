package com.hhs.codeboard.blog.util.common;

import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Component
public class FormatUtil {

    public static String formatDate(LocalDateTime date, String format) {
        return date.format(DateTimeFormatter.ofPattern(format));
    }

}
