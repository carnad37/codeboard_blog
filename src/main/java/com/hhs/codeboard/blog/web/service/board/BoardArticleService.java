package com.hhs.codeboard.blog.web.service.board;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import com.hhs.codeboard.blog.jpa.entity.board.dto.request.BoardArticleRequest;
import com.hhs.codeboard.blog.jpa.entity.board.dto.response.BoardArticleResponse;
import com.hhs.codeboard.blog.jpa.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.jpa.repository.ArticleDAO;
import com.hhs.codeboard.blog.jpa.repository.MenuDAO;
import com.hhs.codeboard.blog.util.service.SearchUtil;
import jakarta.persistence.criteria.Predicate;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleDAO articleDAO;
    private final MenuDAO menuDAO;
    private final ModelMapper modelMapper;

    private final SearchUtil searchUtil;


    public BoardArticleResponse selectArticle(@NotNull Long articleSeq, boolean publicFlag, ) throws CodeboardException {
        return mapBoardArticle(articleDAO.findById(articleSeq).orElseThrow(ErrorType.NOT_FOUND_DATA::supply));
    }


    public BoardArticleResponse selectArticle(@NotNull Long articleSeq) throws CodeboardException {
        return mapBoardArticle(articleDAO.findById(articleSeq).orElseThrow(ErrorType.NOT_FOUND_DATA::supply));
    }

    /**
     * 게시물 검색 쿼리
     * 디폴트로
     * @param boardArticleRequest
     * @return
     */
    public List<BoardArticleResponse> selectArticleList(BoardArticleRequest boardArticleRequest) {
        Specification<BoardArticleEntity> select = searchUtil.findSpecification((root, cb) -> {
            List<Predicate> predicateList = new ArrayList<>();
            if (StringUtils.hasText(boardArticleRequest.getTitle())) {
                predicateList.add(cb.like(root.get("title"), "%" +boardArticleRequest.getTitle() + "%"));
            }
            if (StringUtils.hasText(boardArticleRequest.getSummary())) {
                predicateList.add(cb.like(root.get("summary"), "%" + boardArticleRequest.getSummary() + "%"));
            }
            if (StringUtils.hasText(boardArticleRequest.getContent())) {
                predicateList.add(cb.like(root.get("content"), "%" +boardArticleRequest.getContent() + "%"));
            }
            if (boardArticleRequest.getSearchStartDate() != null) {
                predicateList.add(cb.greaterThanOrEqualTo(root.get("regDate").as(LocalDate.class), boardArticleRequest.getSearchStartDate()));
            }
            if (boardArticleRequest.getSearchEndDate() != null) {
                predicateList.add(cb.lessThanOrEqualTo(root.get("regDate").as(LocalDate.class), boardArticleRequest.getSearchEndDate()));
            }
            if (boardArticleRequest.getCategorySeq() != null && boardArticleRequest.getCategorySeq() > 0) {
                predicateList.add(cb.equal(root.get("categorySeq"), boardArticleRequest.getCategorySeq()));
            }
            if (boardArticleRequest.getCategorySeq() != null && boardArticleRequest.getBoardSeq() > 0) {
                predicateList.add(cb.equal(root.get("boardSeq"), boardArticleRequest.getBoardSeq()));
            }
            predicateList.add(cb.equal(root.get("publicFlag"), boardArticleRequest.getPublicFlag()));
            return predicateList;
        },
        (root, cb, query)->{
            query.orderBy(cb.desc(root.get("regDate")));
        });
        return articleDAO.findAll(select).stream().map(this::mapBoardArticle).toList();
    }

    /**
     * entity, dto 맵핑용
     * @param entity
     * @return
     */
    private BoardArticleResponse mapBoardArticle(BoardArticleEntity entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, BoardArticleResponse.class);
    }

}
