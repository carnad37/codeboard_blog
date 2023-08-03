package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDto;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import lombok.experimental.UtilityClass;
import org.springframework.stereotype.Component;

import java.util.function.Supplier;

@UtilityClass
public class ResponseUtil {

    /**
     * 검색정보가 없는 dto 리턴
     * @param supplier
     * @return
     * @param <T>
     */
    public static <T extends DefaultSearchDto> T getNotSearchDto(Supplier<T> supplier) {
        T result = supplier.get();
        result.setPageIndex(0);
        return result;
    }


}
