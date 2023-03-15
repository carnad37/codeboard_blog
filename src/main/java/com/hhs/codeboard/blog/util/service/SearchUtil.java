package com.hhs.codeboard.blog.util.service;

import com.hhs.codeboard.blog.config.common.TriConsumer;
import com.hhs.codeboard.blog.config.common.TriFunction;
import com.hhs.codeboard.blog.web.service.common.SearchDto;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

@Component
public class SearchUtil {

    public <T> Specification<T> findSpecification(BiFunction<Root, CriteriaBuilder, List<Predicate>> convert, TriConsumer<Root, CriteriaBuilder, CriteriaQuery> queryConsumer) {
        return (root, query, cb) -> {
            List<Predicate> predicateList = convert.apply(root, cb);
            queryConsumer.accept(root, cb, query);
            return cb.and(predicateList.toArray(Predicate[]::new));
        };
    }

    public Specification findSpecificationOrderByRegDate(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
        return (root, query, cb) -> {
            Predicate[] predicateList = process(searchDtoList, root, cb, convert);
            query.orderBy(cb.desc(root.get("regDate")));
            return cb.and(predicateList);
        };
    }

    public Specification findSpecification(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert, TriConsumer<Root, CriteriaBuilder, CriteriaQuery> queryConsumer) {
        return (root, query, cb) -> {
            Predicate[] predicateList = process(searchDtoList, root, cb, convert);
            queryConsumer.accept(root, cb, query);
            return cb.and(predicateList);
        };
    }

//    public Specification findSpecificationOrderByRegDate(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
//        return (root, query, cb) -> {
//            List<Predicate> predicateList = searchDtoList.stream().filter(dto-> StringUtils.hasText(dto.getSearchKeyword())).map(searchDto->convert.apply(root, cb, searchDto)).filter().collect(Collectors.toList());
//            query.orderBy(cb.desc(root.get("regDate")));
//            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
//        };
//    }


    private Predicate[] process(List<SearchDto> searchDtoList, Root root, CriteriaBuilder cb, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
        return searchDtoList.stream()
                .filter(dto-> StringUtils.hasText(dto.getSearchKeyword())).map(searchDto->convert.apply(root, cb, searchDto))
                .filter(Objects::nonNull).toArray(Predicate[]::new);
    }


}
