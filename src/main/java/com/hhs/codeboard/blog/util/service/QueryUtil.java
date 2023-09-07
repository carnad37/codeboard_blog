package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.config.anno.TreeMappingKey;
import com.hhs.codeboard.blog.config.anno.TreeMappingTarget;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.util.common.ReflectionUtils;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.sun.source.tree.Tree;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.util.StringUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.time.temporal.TemporalAccessor;
import java.util.*;
import java.util.function.*;

@UtilityClass
@Slf4j
public class QueryUtil {

    /**
     * 들어온 값이 Null이면 BooleanExpression를 Null로 리턴해서 조건에서 제외
     * 값이 Null이여도 조건에 포함되어야하면 쓰면안됨.
     */
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
     *
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

    public static <T, P extends DefaultSearchDto> List<P> getPageMapping(JPAQuery<T> resultList, Function<List<T>, List<P>> mapping) {
        return mapping.apply(resultList.fetch());
    }

    /**
     * jpa의 구조상 부모값을 부모 엔티티, 자식값을 자식 엔티티 리스트로 설정하지않으면
     * 자동으로 맵핑해주질 않음.(그래도 dinstinct는 해야함)
     *
     * TreeMappingKey 랑 TreeMappingTarget를 이용해서
     * 맵핑가능하게 만든 구조.
     *
     * 해당기능을 이용하면 부모값을 원시값으로 하고도 mybatis의 nested select처럼 사용가능할듯.
     *
     * @param resultList
     * @param mapping
     * @return
     * @param <T>
     * @param <P>
     */
    public static <T, P extends DefaultSearchDto> List<P> getPageTreeMapping(JPAQuery<T> resultList, Function<T, P> mapping) {
        List<T> targetList = resultList.fetch();
        List<P> dataList = new ArrayList<>();

        Map<Object, P> parentMap = new HashMap<>();
        Map<Object, List<P>> childrenMap = new HashMap<>();

        TreeMappingKey treeMappingKey = null;
        String keyName = null;
        Method keyMethod = null;
        String targetName = null;
        Method targetMethod = null;
        try {
            for (int rIdx = targetList.size() - 1; rIdx >= 0; rIdx--) {
                P targetDto = mapping.apply(targetList.get(rIdx));
                parentMap.put(targetDto.getSeq(), targetDto);

                // Dto에서 특정 키가 되는 값의 정의가 필요.
                if  (keyName == null || targetMethod == null) {
                    for (Field tField : targetDto.getClass().getDeclaredFields()) {
                        for (Annotation annotation : tField.getAnnotations()) {
                            if (annotation instanceof TreeMappingKey) {
//                        if (annotation instanceof TreeMappingKey treeMappingKeyData) { // JDK 14부터 가능 호환성 문제
                                treeMappingKey = (TreeMappingKey) annotation;
                                keyName = tField.getName();
                            } else if (annotation instanceof TreeMappingTarget) {
                                targetName = tField.getName();
                            }
                            if (keyName != null && targetName != null) {
                                keyMethod = ReflectionUtils.findGetMethod(targetDto.getClass(), keyName);
                                targetMethod = ReflectionUtils.findSetMethod(targetDto.getClass(), targetName);
                                break;
                            }
                        }
                        if (keyMethod != null && targetMethod != null) break;
                    }

                    if (keyMethod == null || targetMethod == null) return Collections.emptyList();
                }

                Object keyValue = keyMethod.invoke(targetDto);
                if (treeMappingKey.rootValue().equals(String.valueOf(keyValue))) {
                    // 루트인경우
                    dataList.add(0, targetDto);
                } else {
                    // 루트가 아닌경우
                    childrenMap.computeIfAbsent(keyValue, menuSeq -> new ArrayList<>()).add(0, targetDto);
                }

            }

            // dataList도 같은 객체를 보고있으므로 자동으로 채워짐
            for (Map.Entry<Object, List<P>> entry : childrenMap.entrySet()) {
                P targetParent = parentMap.getOrDefault(entry.getKey(), null);
                if (targetParent != null) {
                    targetMethod.invoke(targetParent, entry.getValue());
                }
            }
        } catch (InvocationTargetException | IllegalAccessException ex) {
            return Collections.emptyList();
        }

        return dataList;
    }

}
