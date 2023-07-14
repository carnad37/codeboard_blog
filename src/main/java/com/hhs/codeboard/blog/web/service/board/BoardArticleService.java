package com.hhs.codeboard.blog.web.service.board;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.data.entity.board.entity.QBoardArticleEntity;
import com.hhs.codeboard.blog.enumeration.ErrorType;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.repository.ArticleDAO;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.util.service.QuerySupportUtil;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Transactional
@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleDAO articleDAO;
    private final MenuDAO menuDAO;
    private final ModelMapper modelMapper;
    private final JPAQueryFactory jpaQueryFactory;
    private final QuerySupportUtil queryUtil;

    private final QBoardArticleEntity article = QBoardArticleEntity.boardArticleEntity;

    /**
     * 게시물 저장
     * 차후 유저 정보 포함
     * @param request
     * @return
     */
    public BoardArticleDto saveArticle(BoardArticleDto request) {
        // boardSeq
        BoardArticleEntity entity = articleDAO.findBySeqAndRegUserSeq(request.getSeq(), 1)
                .orElseGet(()->{
                    BoardArticleEntity supEntity = new BoardArticleEntity();
                    supEntity.setRegDate(LocalDateTime.now());
                    supEntity.setRegUserSeq(1L);
                    return supEntity;
                });

        // TODO :: BoardSeq값 유효성 체크 필요
        // 현재 구조로는 타인의 board에 파라미터 위조로 입력가능해짐
        entity.setBoardSeq(request.getBoardSeq());
        entity.setTitle(request.getTitle());
        entity.setSummary(request.getSummary());
        entity.setContent(request.getContent());
        entity.setPublicFlag(request.getPublicFlag());
        entity.setModDate(LocalDateTime.now());
        entity.setModUserSeq(1L);
        //entity.setCategorys(request.getCategorySeq());
        articleDAO.save(entity);
        return mapBoardArticle(entity);
    }

    public BoardArticleDto selectArticle(@NotNull Long articleSeq, boolean publicFlag) throws CodeboardException {
        return mapBoardArticle(articleDAO.findById(articleSeq).orElseThrow(ErrorType.NOT_FOUND_DATA::supply));
    }


    public BoardArticleDto selectArticle(@NotNull Long articleSeq) throws CodeboardException {
        return mapBoardArticle(articleDAO.findById(articleSeq).orElseThrow(ErrorType.NOT_FOUND_DATA::supply));
    }

    /**
     * 게시물 검색 쿼리
     * 디폴트로
     * @param articleRequest
     * @return
     */
    public List<BoardArticleDto> selectArticleList(BoardArticleDto articleRequest) {
        List<BoardArticleEntity> result = jpaQueryFactory.selectFrom(article)
            .where(
                queryUtil.toString(articleRequest.getTitle(), article.title::contains),
                queryUtil.toString(articleRequest.getSummary(), article.summary::contains),
                queryUtil.toString(articleRequest.getContent(), article.content::contains),
                queryUtil.toDate(articleRequest.getSearchStartDate(), time->article.regDate.goe(LocalDateTime.from(time))),
                queryUtil.toDate(articleRequest.getSearchEndDate(), time->article.regDate.loe(LocalDateTime.from(time))),
                queryUtil.toLong(articleRequest.getCategorySeq(), article.categorySeq::eq),
                queryUtil.toLong(articleRequest.getBoardSeq(), article.boardSeq::eq),
                article.publicFlag.eq(YN.N)
            ).orderBy(article.regDate.desc())
            .fetch();

        return result.stream().map(this::mapBoardArticle).toList();
    }

    /**
     * entity, dto 맵핑용
     * @param entity
     * @return
     */
    private BoardArticleDto mapBoardArticle(BoardArticleEntity entity) {
        if (entity == null) return null;
        return modelMapper.map(entity, BoardArticleDto.class);
    }

}
