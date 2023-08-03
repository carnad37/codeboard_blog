//package com.hhs.codeboard.blog.util.service;
//
//import com.hhs.codeboard.blog.config.common.TriConsumer;
//import com.hhs.codeboard.blog.config.common.TriFunction;
//import com.hhs.codeboard.blog.web.service.common.SearchDto;
//import jakarta.persistence.criteria.CriteriaBuilder;
//import jakarta.persistence.criteria.CriteriaQuery;
//import jakarta.persistence.criteria.Predicate;
//import jakarta.persistence.criteria.Root;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.function.BiFunction;
//
/**
 * Specification 관련 유틸기능
 * annotation이랑 같이 사용함으로써 어느정되 최적화가 가능하지만,
 * 더 깊게 파도 queryDSL이 있는 이상 쓸데없는 공수로 보여서 작업 중단.
 */
//@Component
//public class SearchUtil {
//
//    public <T> Specification<T> findSpecification(BiFunction<Root, CriteriaBuilder, List<Predicate>> convert, TriConsumer<Root, CriteriaBuilder, CriteriaQuery> queryConsumer) {
//        return (root, query, cb) -> {
//            List<Predicate> predicateList = convert.apply(root, cb);
//            queryConsumer.accept(root, cb, query);
//            return cb.and(predicateList.toArray(Predicate[]::new));
//        };
//    }
//
//    public Specification findSpecificationOrderByRegDate(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
//        return (root, query, cb) -> {
//            Predicate[] predicateList = process(searchDtoList, root, cb, convert);
//            query.orderBy(cb.desc(root.get("regDate")));
//            return cb.and(predicateList);
//        };
//    }
//
//    public Specification findSpecification(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert, TriConsumer<Root, CriteriaBuilder, CriteriaQuery> queryConsumer) {
//        return (root, query, cb) -> {
//            Predicate[] predicateList = process(searchDtoList, root, cb, convert);
//            queryConsumer.accept(root, cb, query);
//            return cb.and(predicateList);
//        };
//    }
//
////    public Specification findSpecificationOrderByRegDate(List<SearchDto> searchDtoList, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
////        return (root, query, cb) -> {
////            List<Predicate> predicateList = searchDtoList.stream().filter(dto-> StringUtils.hasText(dto.getSearchKeyword())).map(searchDto->convert.apply(root, cb, searchDto)).filter().collect(Collectors.toList());
////            query.orderBy(cb.desc(root.get("regDate")));
////            return cb.and(predicateList.toArray(new Predicate[predicateList.size()]));
////        };
////    }
//
//
//    private Predicate[] process(List<SearchDto> searchDtoList, Root root, CriteriaBuilder cb, TriFunction<Root, CriteriaBuilder, SearchDto, Predicate> convert) {
//        return searchDtoList.stream()
//                .filter(dto-> StringUtils.hasText(dto.getSearchKeyword())).map(searchDto->convert.apply(root, cb, searchDto))
//                .filter(Objects::nonNull).toArray(Predicate[]::new);
//    }
//
//
//}

// 실제 구현코드
//Specification<BoardArticleEntity> select = searchUtil.findSpecification((root, cb) -> {
//        List<Predicate> predicateList = new ArrayList<>();
//        if (StringUtils.hasText(boardArticleRequest.getTitle())) {
//            predicateList.add(cb.like(root.get("title"), "%" +boardArticleRequest.getTitle() + "%"));
//        }
//        if (StringUtils.hasText(boardArticleRequest.getSummary())) {
//            predicateList.add(cb.like(root.get("summary"), "%" + boardArticleRequest.getSummary() + "%"));
//        }
//        if (StringUtils.hasText(boardArticleRequest.getContent())) {
//            predicateList.add(cb.like(root.get("content"), "%" +boardArticleRequest.getContent() + "%"));
//        }
//        if (boardArticleRequest.getSearchStartDate() != null) {
//            predicateList.add(cb.greaterThanOrEqualTo(root.get("regDate").as(LocalDate.class), boardArticleRequest.getSearchStartDate()));
//        }
//        if (boardArticleRequest.getSearchEndDate() != null) {
//            predicateList.add(cb.lessThanOrEqualTo(root.get("regDate").as(LocalDate.class), boardArticleRequest.getSearchEndDate()));
//        }
//        if (boardArticleRequest.getCategorySeq() != null && boardArticleRequest.getCategorySeq() > 0) {
//            predicateList.add(cb.equal(root.get("categorySeq"), boardArticleRequest.getCategorySeq()));
//        }
//        if (boardArticleRequest.getCategorySeq() != null && boardArticleRequest.getBoardSeq() > 0) {
//            predicateList.add(cb.equal(root.get("boardSeq"), boardArticleRequest.getBoardSeq()));
//        }
//        predicateList.add(cb.equal(root.get("publicFlag"), boardArticleRequest.getPublicFlag()));
//        return predicateList;
//    },
//    (root, cb, query)->query.orderBy(cb.desc(root.get("regDate")))
//);