package com.hhs.codeboard.blog.web.service.board;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.config.except.CodeboardParameterException;
import com.hhs.codeboard.blog.config.except.NotFoundDataException;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleContentEntity;
import com.hhs.codeboard.blog.data.entity.board.entity.QBoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.QMenuEntity;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.repository.ArticleDAO;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.util.common.FormatUtil;
import com.hhs.codeboard.blog.util.service.QueryUtil;
import com.hhs.codeboard.blog.util.service.SecurityUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;

@Transactional
@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleDAO articleDAO;
    private final MenuDAO menuDAO;
    private final JPAQueryFactory jpaQueryFactory;
    private final QMenuEntity menu = QMenuEntity.menuEntity;
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
        entity.setPublicFlag(request.getPublicFlag().getCode());
        entity.setModDate(LocalDateTime.now());
        entity.setModUserSeq(1L);

        //entity.setCategorys(request.getCategorySeq());
        articleDAO.save(entity);
        return mapBoardArticle(entity);
    }


    public BoardArticleDto selectArticle(@NotNull Long articleSeq) throws CodeboardException {
        // validate
        if (!FormatUtil.Number.isPositive(articleSeq)) {
            throw new CodeboardParameterException("잘못된 파라미터입니다.");
        }

        MemberDto memberDto = SecurityUtil.getUser();

        BoardArticleDto selectVO = new BoardArticleDto();
        selectVO.setSeq(articleSeq);

        // condition
        BooleanExpression[] whereArray = {
            article.delDate.isNull()
            , QueryUtil.longNullable(articleSeq, article.seq::eq)
            , this.memberCondition(memberDto)
        };

        return Optional.ofNullable(
                    jpaQueryFactory.selectFrom(article)
                        .where(whereArray)
                        .fetchOne()
            ).map(this::mapBoardArticle)
            .orElseThrow(()->new NotFoundDataException("게시물을 찾을수 없습니다"));
    }

    /**
     * 게시물 검색 쿼리
     * 디폴트로
     * @param articleRequest
     * @return
     */
    public List<BoardArticleDto> selectArticleList(BoardArticleDto articleRequest) {
        // validate
        MemberDto memberDto = SecurityUtil.getUser();

        List<BooleanExpression> whereList = Arrays.asList(
                QueryUtil.stringNullable(articleRequest.getTitle(), article.title::contains),
                QueryUtil.stringNullable(articleRequest.getSummary(), article.summary::contains),
                QueryUtil.stringNullable(articleRequest.getContent(), article.content::contains),
                QueryUtil.longNullable(articleRequest.getCategorySeq(), article.categorySeq::eq),
                QueryUtil.longNullable(articleRequest.getBoardSeq(), article.boardSeq::eq),
                QueryUtil.dateNullable(articleRequest.getSearchStartDate(), time -> article.regDate.goe(LocalDateTime.from(time))),
                QueryUtil.dateNullable(articleRequest.getSearchEndDate(), time -> article.regDate.loe(LocalDateTime.from(time))),
                this.memberCondition(memberDto)
        );

        List<BoardArticleEntity> result = jpaQueryFactory.selectFrom(article)
            .where(
                whereList.stream().filter(Objects::nonNull).toArray(BooleanExpression[]::new)
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
        BoardArticleDto articleDto = new BoardArticleDto();
        articleDto.setSeq(entity.getSeq());
        articleDto.setBoardSeq(entity.getBoardSeq());
        articleDto.setTitle(entity.getTitle());

        articleDto.setSummary(entity.getSummary());
        articleDto.setContent(entity.getContent());

        articleDto.setRegDate(entity.getRegDate());
        articleDto.setModDate(entity.getModDate());
        articleDto.setDelDate(entity.getDelDate());

        return articleDto;
    }

    /**
     * 회원전용 조건 체크
     * 해당 vo는 반드시 로그인 여부를 체크해야함
     * @param memberDto
     * @return
     */
    private BooleanExpression memberCondition(MemberDto memberDto) {
        if (memberDto != null) {
            return article.publicFlag.eq(YN.Y.getCode()).or(
                    article.publicFlag.eq(YN.N.getCode()).and(article.regUserSeq.eq(memberDto.getUserSeq()))
            );
        } else {
            // 로그인 안한상태
            return article.publicFlag.eq(YN.Y.getCode());
        }
    }



}
