//package com.hhs.codeboard.blog.web.service.board;
//
//import com.hhs.codeboard.blog.config.common.TriFunction;
//import com.hhs.codeboard.blog.jpa.entity.board.dto.BoardArticleRequest;
//import com.hhs.codeboard.blog.jpa.entity.board.entity.BoardArticleEntity;
//import com.hhs.codeboard.blog.jpa.repository.ArticleDAO;
//import com.hhs.codeboard.blog.jpa.repository.MenuDAO;
//import com.hhs.codeboard.blog.web.service.common.SearchDto;
//import lombok.RequiredArgsConstructor;
//import org.hibernate.cache.spi.access.UnknownAccessTypeException;
//import org.modelmapper.ModelMapper;
//import org.springframework.data.jpa.domain.Specification;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.StringUtils;
//
//import java.time.LocalDate;
//import java.time.LocalDateTime;
//import java.util.List;
//
//@Transactional
//@Service
//@RequiredArgsConstructor
//public class BoardArticleService {
//
//    private final ArticleDAO articleDAO;
//    private final MenuDAO menuDAO;
//    private final ModelMapper modelMapper;
//
//    public List<BoardArticleRequest> selectArticle(List<SearchDto> searchDtoList) {
//        Specification<BoardArticleRequest> resultQuery = null;
//        for (SearchDto searchDto : searchDtoList) {
//            if (StringUtils.hasText(searchDto.getSearchKeyword())) {
//                switch (searchDto.getSearchCondition()) {
//                    case "title", "content" -> resultQuery = permitAdd(resultQuery, Compare.LIKE, searchDto, BoardArticleRequest.class);
//                    case "publicFlag" -> resultQuery = permitAdd(resultQuery, Compare.EQUAL, searchDto, BoardArticleRequest.class);
//                    case "startDate" -> resultQuery = permitAdd(resultQuery, Compare.EQUAL_GREATER, searchDto, BoardArticleRequest.class);
//                    case "endDate" -> resultQuery = permitAdd(resultQuery, Compare.EQUAL_LESS, searchDto, BoardArticleRequest.class);
//                }
//            }
//        }
//        return articleDAO.findAll(resultQuery);
//    }
//
//    /**
//     * entity, dto 맵핑용
//     * @param entity
//     * @return
//     */
//    private BoardArticleRequest mapBoardArticleRequest(BoardArticleEntity entity) {
//        if (entity == null) return null;
//        return modelMapper.map(entity, BoardArticleRequest.class);
//    }
//
//    private Specification<BoardArticleRequest> permitAdd(Specification<BoardArticleRequest> root, Compare tCompare, SearchDto searchDto, Class<?> mapClassInfo) {
//        Specification<BoardArticleRequest> element = tCompare.process.apply(searchDto.getSearchCondition(), searchDto.getSearchKeyword(), mapClassInfo);
//        if (root == null) return Specification.where(element);
//        else return root.and(element);
//    }
//
//    @RequiredArgsConstructor
//    protected enum Compare {
//        GREATER ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(String.class), data);
//            } else if (LocalDate.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDate.class), getLocalDate(data));
//            } else if (LocalDateTime.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDateTime.class), getLocalDateTime(data));
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        , LESS ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(String.class), data);
//            } else if (LocalDate.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDate.class), getLocalDate(data));
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        , EQUAL ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(String.class), data);
//            } else if (LocalDate.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDate.class), getLocalDate(data));
//            } else if (LocalDateTime.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDateTime.class), getLocalDateTime(data));
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        , EQUAL_GREATER ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(String.class), data);
//            } else if (LocalDate.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDate.class), getLocalDate(data));
//            } else if (LocalDateTime.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDateTime.class), getLocalDateTime(data));
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        , EQUAL_LESS ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(String.class), data);
//            } else if (LocalDate.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDate.class), getLocalDate(data));
//            } else if (LocalDateTime.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.greaterThan(root.get(column).as(LocalDateTime.class), getLocalDateTime(data));
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        , LIKE ((column, data, classInfo) -> {
//            Class<?> tClassInfo = getClassInfo(classInfo, column);
//            if (String.class == tClassInfo) {
//                return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get(column).as(String.class),  "%" + data + "%");
//            } else {
//                throw new UnknownAccessTypeException(data.getClass().getTypeName());
//            }
//        })
//        ;
//
//        private static Class<?> getClassInfo(Class<?> parentClassInfo, String column) {
//            try {
//                return parentClassInfo.getField(column).getType();
//            } catch (NoSuchFieldException e) {
//                throw new UnknownAccessTypeException(parentClassInfo.getTypeName());
//            }
//        }
//
//        private static LocalDate getLocalDate(String date) {
//            return LocalDate.parse(date);
//        }
//
//        private static LocalDateTime getLocalDateTime(String date) {
//            return LocalDateTime.parse(date);
//        }
//
//        private final TriFunction<String, String, Class<?>, Specification<BoardArticleRequest>> process;
//
//    }
//
//    @RequiredArgsConstructor
//    protected enum DbType {
//        STRING ()
//        , INTEGER ()
//        , LOCAL_DATE ()
//        , LOCAL_DATE_TIME ();
//    }
//
//
////    public static Specification<? extends DefaultDateDto> inContent(String date) {
////        return (root, query, criteriaBuilder) -> criteriaBuilder.like(root.get("publicFlag"), date);
////    }
//
//}
