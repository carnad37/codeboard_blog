package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import lombok.experimental.UtilityClass;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.time.temporal.TemporalAccessor;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongFunction;
import java.util.function.LongSupplier;

@UtilityClass
public class QueryUtil {

    public static BooleanExpression stringNullable(String target , Function<String, BooleanExpression> func) {
        return StringUtils.hasText(target) ? func.apply(target) : null;
    }

    public static <T extends TemporalAccessor> BooleanExpression dateNullable(T target , Function<T, BooleanExpression> func) {
        return target != null ? func.apply(target) : null;
    }

    public static BooleanExpression integerNullable(Integer target , IntFunction<BooleanExpression> func) {
        return target != null && target > 0 ? func.apply(target) : null;
    }
    public static BooleanExpression longNullable(Long target , LongFunction<BooleanExpression> func) {
        return target != null && target > 0 ? func.apply(target) : null;
    }

    public static BooleanExpression objectNullable(Object target , Function<Object, BooleanExpression> func) {
        return target != null ? func.apply(target) : null;
    }

    /**
     * pageable에서 offset을 꺼내고 다시 그걸또 파라미터로 넘기는게 이상해서 만든 기능.
     * offset같은 slice기능은 모두 해당 기능에서 작동.
     * @param contentList
     * @param totalCount
     * @param request 페이지 정보를 위해 가져옴
     * @param mapping
     * @return
     * @param <T>
     * @param <P>
     */
    public static <T, P extends DefaultSearchDto> Page<P> getPage(JPAQuery<T> contentList, LongSupplier totalCount, P request, Function<T, P> mapping) {
        Pageable pageable = PageRequest.of(Math.max(request.getPageIndex() - 1, 0), request.getPageSize());
        Page<T> result = PageableExecutionUtils.getPage(contentList.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch()
                , pageable
                , totalCount);
        return result.map(mapping);
    }
    public static <T, P extends DefaultSearchDto> Page<P> getPage(JPAQuery<T> contentList, JPAQuery<Long> totalCount, P request, Function<T, P> mapping) {
        return getPage(contentList, totalCount::fetchOne, request, mapping);
    }
}
