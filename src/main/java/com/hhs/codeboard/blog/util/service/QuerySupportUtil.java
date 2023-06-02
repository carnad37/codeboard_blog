package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.time.temporal.TemporalAccessor;
import java.util.function.Function;
import java.util.function.IntFunction;
import java.util.function.LongSupplier;

@Component
public class QuerySupportUtil {

    public BooleanExpression string(String target , Function<String, BooleanExpression> func) {
        return StringUtils.hasText(target) ? func.apply(target) : null;
    }

    public <T extends TemporalAccessor> BooleanExpression date(T target , Function<T, BooleanExpression> func) {
        return target != null ? func.apply(target) : null;
    }

    public BooleanExpression integer(Integer target , IntFunction<BooleanExpression> func) {
        return target != null && target > 0 ? func.apply(target) : null;
    }

    public BooleanExpression object(Object target , Function<Object, BooleanExpression> func) {
        return target != null ? func.apply(target) : null;
    }

    /**
     * pageable에서 offset을 꺼내고 다시 그걸또 파라미터로 넘기는게 이상해서 만든 기능.
     * offset같은 slice기능은 모두 해당 기능에서 작동.
     * @param contentList
     * @param request
     * @param totalCount
     * @param mapping
     * @return
     * @param <T>
     * @param <P>
     */
    public <T, P extends DefaultSearchDto> Page<P> getPage(JPAQuery<T> contentList, P request, LongSupplier totalCount, Function<T, P> mapping) {
        Pageable pageable = PageRequest.of(Math.max(request.getPageIndex() - 1, 0), request.getPageSize());
        Page<T> result = PageableExecutionUtils.getPage(contentList.offset(pageable.getOffset()).limit(pageable.getPageSize()).fetch()
                , pageable
                , totalCount);
        return result.map(mapping);
    }
}
