package com.hhs.codeboard.blog.web.service.board;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.config.except.CodeboardParameterException;
import com.hhs.codeboard.blog.config.except.NotFoundDataException;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleContentDto;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleContentEntity;
import com.hhs.codeboard.blog.data.entity.board.entity.QBoardArticleContentEntity;
import com.hhs.codeboard.blog.data.entity.board.entity.QBoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.data.entity.menu.entity.QMenuEntity;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.repository.ArticleContentDAO;
import com.hhs.codeboard.blog.data.repository.ArticleDAO;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.enumeration.EditorType;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.util.common.EnumUtil;
import com.hhs.codeboard.blog.util.common.FormatUtil;
import com.hhs.codeboard.blog.util.service.QueryUtil;
import com.hhs.codeboard.blog.util.service.SecurityUtil;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.CollectionUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.LongSupplier;

@Transactional
@Service
@RequiredArgsConstructor
public class BoardArticleService {

    private final ArticleDAO articleDAO;
    private final MenuDAO menuDAO;
    private final MenuService menuService;
    private final ArticleContentDAO contentDAO;
    private final JPAQueryFactory jpaQueryFactory;

    private final QMenuEntity menu = QMenuEntity.menuEntity;
    private final QBoardArticleEntity article = QBoardArticleEntity.boardArticleEntity;
    private final QBoardArticleContentEntity articleContent = QBoardArticleContentEntity.boardArticleContentEntity;

    /**
     * 게시물 저장
     * 차후 유저 정보 포함
     * @param request
     * @return
     */
    public BoardArticleDto saveArticle(BoardArticleDto request) {
        Long userSeq = SecurityUtil.getUserSeq();

        // 수정일 경우 seq값이 있고, 아닐경우 없음
        BoardArticleEntity entity;
        if (FormatUtil.Number.isPositive(request.getSeq())) {
            // articleSeq
            entity = articleDAO.findBySeqAndRegUserSeq(request.getSeq(), userSeq)
                    .orElseThrow(()->new CodeboardParameterException("게시물 정보가 잘못되었습니다."));
        } else {
            // 새등록일 경우
            entity = new BoardArticleEntity();
            entity.setRegDate(LocalDateTime.now());
            entity.setRegUserSeq(userSeq);
            // board값 체크필요.
            MenuDto menuDto = new MenuDto();
            menuDto.setSeq(request.getBoardSeq());

            MemberDto memberDto = new MemberDto();
            memberDto.setUserSeq(userSeq);

            if (Objects.isNull(menuService.getMenu(menuDto, memberDto))) {
                throw new CodeboardParameterException("잘못된 접근입니다.");
            }

            entity.setBoardSeq(request.getBoardSeq());
        }

        // TODO :: BoardSeq값 유효성 체크 필요
        entity.setTitle(request.getTitle());
        entity.setSummary(request.getSummary());
        entity.setContent(request.getContent());
        entity.setPublicFlag(request.getPublicFlag().getCode());
        entity.setModDate(LocalDateTime.now());
        entity.setModUserSeq(1L);

        // entity.setCategorys(request.getCategorySeq());
        articleDAO.save(entity);

        // 하위 컨텐츠 처리
        Optional.of(request)
            .map(BoardArticleDto::getUploadContents)
            .ifPresent(contents->{
                if (!CollectionUtils.isEmpty(contents.getInsert())) {
                    // bulk insert는 jdbc Template를 이용하거나 Jpa의 saveAll을 이용하는방법이 있는데, saveAll도 결국은 한땀한땀 insert
                    // bulk insert를 jdbc로 썼다간 orm의 의미가 없는거같아서 그냥 saveAll 단일쿼리로 처리
                    contentDAO.saveAll(
                        contents.getInsert().stream()
                            .map(target->this.saveBoardArticleContent(target, entity))
                            .toList()
                    );
                }

                if (!CollectionUtils.isEmpty(contents.getUpdate())) {
                    contents.getUpdate()
                        .forEach(target->{
                            BoardArticleContentEntity contentEntity = this.saveBoardArticleContent(target, entity);
                            contentDAO.save(contentEntity);
                        });
                }

                if (!CollectionUtils.isEmpty(contents.getDelete())) {
                    jpaQueryFactory.update(articleContent)
                        .set(articleContent.delDate, LocalDateTime.now())
                        .where(
                            articleContent.article.eq(entity),
                            articleContent.seq.in(
                                contents.getDelete().stream()
                                    .map(BoardArticleContentDto::getSeq)
                                    .filter(FormatUtil.Number::isPositive)
                                    .toList()
                            )
                        )
                        .execute();
                }
            });
        return mapBoardArticle(entity, false);
    }


    public BoardArticleDto selectArticle(@NotNull Long articleSeq) throws CodeboardException {
        // validate
        if (!FormatUtil.Number.isPositive(articleSeq)) {
            throw new CodeboardParameterException("잘못된 파라미터입니다.");
        }

        MemberDto memberDto = SecurityUtil.getUser();

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
            ).map(target->this.mapBoardArticle(target, true))
            .orElseThrow(()->new NotFoundDataException("게시물을 찾을수 없습니다"));
    }

    /**
     * 게시물 검색 쿼리
     * 디폴트로
     * @param articleRequest
     * @return
     */
    public Page<BoardArticleDto> selectArticleList(BoardArticleDto articleRequest) {
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

        JPAQuery<BoardArticleEntity> result = jpaQueryFactory.selectFrom(article)
            .where(
                whereList.stream().filter(Objects::nonNull).toArray(BooleanExpression[]::new)
            );

        LongSupplier totalCnt = ()->jpaQueryFactory.select(article.count())
            .from(article)
            .where(
                    whereList.stream().filter(Objects::nonNull).toArray(BooleanExpression[]::new)
            ).fetchFirst();

        return QueryUtil.getPage(result, totalCnt, articleRequest, (target)->this.mapBoardArticle(target, false));
    }

    /**
     * entity, dto 맵핑용
     * @param entity
     * @return
     */
    private BoardArticleDto mapBoardArticle(BoardArticleEntity entity, boolean callChildren) {
        if (entity == null) return null;
        BoardArticleDto articleDto = new BoardArticleDto();
        articleDto.setSeq(entity.getSeq());
        articleDto.setBoardSeq(entity.getBoardSeq());
        articleDto.setTitle(entity.getTitle());
        articleDto.setPublicFlag(EnumUtil.covertCodeboardEnum(YN.class, entity.getPublicFlag()));

        articleDto.setSummary(entity.getSummary());
        articleDto.setContent(entity.getContent());

        articleDto.setRegDate(entity.getRegDate());
        articleDto.setModDate(entity.getModDate());
        articleDto.setDelDate(entity.getDelDate());

        if (callChildren) articleDto.setContents(entity.getContents().stream().map(this::mapBoardArticleContent).toList());

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

    private BoardArticleContentDto mapBoardArticleContent(BoardArticleContentEntity entity) {
        BoardArticleContentDto contentDto = new BoardArticleContentDto();
        contentDto.setSeq(entity.getSeq());
        contentDto.setContent(entity.getContent());
        contentDto.setEditor(EnumUtil.covertCodeboardEnum(EditorType.class, entity.getEditor()));
        contentDto.setContentOrder(entity.getContentOrder());
        return contentDto;
    }

    private BoardArticleContentEntity saveBoardArticleContent(BoardArticleContentDto dto, BoardArticleEntity articleEntity) {
        BoardArticleContentEntity entity;
        LocalDateTime toDate = LocalDateTime.now();
        if (FormatUtil.Number.isPositive(dto.getSeq())) {
            // update, delete
            // 해당 seq에 맞는 정보 return
            entity = jpaQueryFactory
                .selectFrom(articleContent)
                .where(
                    articleContent.seq.eq(dto.getSeq()),
                    articleContent.article.eq(articleEntity),
                    articleContent.delDate.isNull()
                ).fetchOne();
            if (entity != null) {
                entity.setContent(dto.getContent());
                entity.setContentOrder(dto.getContentOrder());
                entity.setEditor(dto.getEditor().getCode());
            }
        } else {
            // insert
            entity = new BoardArticleContentEntity();
            entity.setArticle(articleEntity);
            entity.setContentOrder(dto.getContentOrder());
            entity.setEditor(dto.getEditor().getCode());
            entity.setContent(dto.getContent());
            entity.setRegDate(toDate);
        }

        return entity;
    }



}
