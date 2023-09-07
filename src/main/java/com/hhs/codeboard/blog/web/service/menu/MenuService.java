package com.hhs.codeboard.blog.web.service.menu;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.config.except.CodeboardParameterException;
import com.hhs.codeboard.blog.config.except.NotFoundDataException;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.QMenuEntity;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.util.common.EnumUtil;
import com.hhs.codeboard.blog.util.common.FormatUtil;
import com.hhs.codeboard.blog.util.service.QueryUtil;
import com.hhs.codeboard.blog.util.service.ResponseUtil;
import com.hhs.codeboard.blog.util.service.SecurityUtil;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.querydsl.core.types.Predicate;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.util.*;
import java.util.List;
import java.util.function.Supplier;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuDAO menuDAO;
    private final JPAQueryFactory jpaQueryFactory;

    private final QMenuEntity menu = QMenuEntity.menuEntity;
    private final QMenuEntity joinMenu =  new QMenuEntity("joinMenu");;

    /**
     * 단일 객체 확인
     *
     * 단일 객체에선 타인것도 조회 가능해야함.
     * 다만 타인건 무조건 공개된것만.
     * @param menuDto
     * @return
     * @throws RuntimeException
     */
    public MenuDto selectOne(MenuDto menuDto, boolean publicFlag) throws CodeboardException {
        if (menuDto.getSeq() == null || menuDto.getSeq() < 1) {
            throw new CodeboardParameterException("타겟 정보가 없습니다.");
        }

        Predicate[] wheres = getDefaultConditionToArray(menuDto);

        return Optional.ofNullable(
                jpaQueryFactory.selectFrom(menu)
                    .where(wheres)
                    .fetchOne())
            .map(target->mapMenu(target, publicFlag))
            .orElseThrow(()->new RuntimeException("타겟 메뉴 정보가 없습니다."));
    }

    /**
     * 전체 메뉴 조회
     * @param menuDto
     * @return
     * @throws CodeboardException
     */
    public List<MenuDto> selectAll(MenuDto menuDto, boolean publicFlag) throws CodeboardException {
        // validate
        if (!FormatUtil.Number.isPositive(menuDto.getUserSeq())) {
            throw new CodeboardParameterException("잘못된 요청입니다.");
        }

        if (menuDto.getParentSeq() == 0L) {
            Predicate[] wheres = getDefaultConditionToArray(menuDto);

            JPAQuery<MenuEntity> queryList = jpaQueryFactory.selectFrom(menu)
                    .leftJoin(menu.childrenList, joinMenu)
                    .fetchJoin()
                    .where(wheres);
//            return QueryUtil.getPageTreeMapping(queryList , target->mapMenu(target, publicFlag));
            return queryList.distinct().fetch().stream().map(target->mapMenu(target, publicFlag)).collect(Collectors.toList());
        } else {
            Predicate[] wheres = getDefaultConditionToArray(menuDto);

            JPAQuery<MenuEntity> queryList = jpaQueryFactory.selectFrom(menu)
                    .where(wheres);
//            return queryList.fetch().stream().map(target->this.mapMenu(target, publicFlag)).toList();
            return queryList.distinct().fetch().stream().map(target->mapMenu(target, publicFlag)).collect(Collectors.toList());
        }
    }

    /**
     * 메뉴가 있는지 확인
     * @param menuDto
     * @return
     */
    public boolean existMenu(MenuDto menuDto) {
//        commonSelectChecker(menuDto);
        Predicate[] wheres = getDefaultConditionToArray(menuDto);
        return jpaQueryFactory.selectOne().from(menu).where(wheres).fetchOne() != null;
    }


    /**
     * 메뉴 저장
     * @param menuDto
     * @throws CodeboardException
     */
    public MenuDto insertMenu(MenuDto menuDto) throws CodeboardException {
        MenuTypeEnum menuType = menuDto.getMenuType();

        // validate
        // parameter
        if (!MenuTypeEnum.BOARD.equals(menuType) && !MenuTypeEnum.MENU.equals(menuType)) {
            throw new CodeboardParameterException("허용되지 않은 타입입니다.");
        } else if (!SecurityUtil.isLogin()) {
            throw new CodeboardParameterException("로그인이 되어있지 않습니다.");
        }

        MemberDto memberDto = SecurityUtil.getUser();

        MenuEntity insert = new MenuEntity();

        if (!FormatUtil.Number.isPositive(menuDto.getParentSeq())) {
            // root인경우
            // 부모값 세팅
            menuDto.setParentSeq(null);
        } else {
            // 부모가 본인 게시판인지 확인
            MenuEntity parentMenu = getParentMenu(menuDto, memberDto);
            if (Objects.isNull(parentMenu)) {
                throw new CodeboardParameterException("잘못된 접근입니다");
            } else {
                insert.setParent(parentMenu);
            }
        }

        insert.setTitle(menuDto.getTitle());
        insert.setMenuType(menuDto.getMenuType().getCode());
        insert.setPublicFlag(menuDto.getPublicFlag().getCode());
        insert.setMenuOrder(menuDto.getMenuOrder());
        insert.setRegUserSeq(memberDto.getUserSeq());
        insert.setRegDate(LocalDateTime.now());
        insert.setUuid(Pattern.compile("-").matcher(UUID.randomUUID().toString()).replaceAll(""));
        insert = menuDAO.save(insert);

        MenuDto response = ResponseUtil.getNotSearchDto(MenuDto::new);
        response.setSeq(insert.getSeq());
        response.setUuid(insert.getUuid());
        return response;
    }

    /**
     *
     * @param menuDto
     * @throws CodeboardException
     * @return
     */
    public MenuDto updateMenu(MenuDto menuDto) throws CodeboardException {

        // validate

        // parameter
        if (!SecurityUtil.isLogin()) {
            throw new CodeboardParameterException("로그인이 되어있지 않습니다.");
        } else if (
                !StringUtils.hasText(menuDto.getTitle())
                || Objects.isNull(menuDto.getPublicFlag())
        ) {
            throw new CodeboardParameterException("잘못된 요청입니다.");
        }
        MemberDto memberDto = SecurityUtil.getUser();

        // 메뉴 확인
        MenuEntity targetMenu = getMenu(menuDto, memberDto);
        if (targetMenu == null) throw new NotFoundDataException("게시물을 찾을 수 없습니다.");

        // 변경 가능한 파리미터
        targetMenu.setTitle(menuDto.getTitle());
        targetMenu.setPublicFlag(menuDto.getPublicFlag().getCode());
        targetMenu.setModDate(LocalDateTime.now());
        targetMenu.setModUserSeq(memberDto.getUserSeq());
        menuDAO.save(targetMenu);

        return menuDto;
    }

    /**
     * 메뉴 삭제(하위메뉴 체크)
     * @param menu
     * @param memberDto
     * @throws CodeboardException
     */
    public void deleteMenu(MenuDto menu, MemberDto memberDto, MenuTypeEnum targetType) throws CodeboardException {
        MenuEntity deleteVO = getMenu(menu, memberDto);
        if (!targetType.getCode().equals(deleteVO.getMenuType())) {
            throw new ServiceException("타겟타입이 아닙니다.");
        } else if (targetType.equals(MenuTypeEnum.MENU) && !menuDAO.findAllByParentSeqAndDelDateIsNull(deleteVO.getSeq()).isEmpty()) {
            //메뉴타입일 경우에만 하위메뉴 체크
            throw new ServiceException("하위 메뉴가 있습니다.");
        } else {
            //자식 메뉴가 하나라도 있을경우 삭제불가
            //실제 삭제안하고 delDate만 업데이트
            deleteVO.setDelDate(LocalDateTime.now());
            menuDAO.save(deleteVO);
        }
    }


    /**
     * uuid 또는 id로 menuEntity 선택
     * @param menuDto
     * @param member
     * @return
     * @throws Exception
     */
    private MenuEntity getMenu(MenuDto menuDto, MemberDto member) throws CodeboardException {
        BooleanExpression[] whereArray = {
                null        // 변경조건 들어갈자리
                , menu.delDate.isNull()
                , QueryUtil.longNullable(member.getUserSeq(), menu.regUserSeq::eq)
        };
        if (StringUtils.hasText(menuDto.getUuid())) {
            whereArray[0] = menu.uuid.eq(menuDto.getUuid());
        } else if (menuDto.getSeq() != null && menuDto.getSeq() > 0) {
            whereArray[0] = menu.seq.eq(menuDto.getSeq());
        } else {
            throw new CodeboardParameterException("잘못된 파라미터 입니다.");
        }

        return jpaQueryFactory
                .selectFrom(menu)
                .where(whereArray)
                .fetchOne();
    }

    private void checkParentMenu(long parentSeq) throws RuntimeException {
        // 메뉴수정을 위해 상위메뉴 정보를 체크하는 기능이므로 반드시 로그인 여부가 필요.
        // parentSeq 체크, 없으면 exception
        MenuDto menuDto = new MenuDto();
        menuDto.setRegUserSeq(SecurityUtil.getUserSeq());
        menuDto.setSeq(parentSeq);
        if (!existMenu(menuDto)) throw new RuntimeException("상위 메뉴가 없습니다.");
    }

    private List<Predicate> getDefaultCondition(MenuDto menuDto) {
        MenuEntity parent = null;

        BooleanExpression parentSeqExpress = null;
        if (FormatUtil.Number.isPositive(menuDto.getParentSeq()))  {
            MemberDto memberDto = new MemberDto();
            if (FormatUtil.Number.isPositive(menuDto.getParentSeq())) memberDto.setUserSeq(menuDto.getUserSeq());
            parent = getParentMenu(menuDto, memberDto);
            parentSeqExpress = menu.parent.eq(parent);
        } else {
            parentSeqExpress =  menu.parent.isNull();
        }

        return Arrays.asList(
                QueryUtil.longNullable(menuDto.getSeq(), menu.seq::eq),
                QueryUtil.longNullable(menuDto.getUserSeq(), menu.regUserSeq::eq),
                parentSeqExpress,
                SecurityUtil.isLogin() ?
                    menu.publicFlag.eq(YN.Y.getCode()).or(
                        menu.publicFlag.eq(YN.N.getCode()).and(menu.regUserSeq.eq(SecurityUtil.getUserSeq()))
                    )
                    :
                    menu.publicFlag.eq(YN.Y.getCode())
                ,
                menu.delDate.isNull()
        );
    }

    private Predicate[] getDefaultConditionToArray(MenuDto menuDto) {
        List<Predicate> result = getDefaultCondition(menuDto);
        return result.toArray(new Predicate[0]);
    }

    /**
     * entity, dto 맵핑용
     * @param entity
     * @return
     */
    private MenuDto mapMenu(MenuEntity entity, boolean publicFlag) {
        if (entity == null) return null;
        MenuDto menuDto = new MenuDto();
        menuDto.setTitle(entity.getTitle());
        menuDto.setSeq(entity.getSeq());
//        menuDto.setParentSeq(entity.getParentSeq());

        menuDto.setMenuType(EnumUtil.covertCodeboardEnum(MenuTypeEnum.class, entity.getMenuType()));
        menuDto.setMenuOrder(entity.getMenuOrder());
        menuDto.setUuid(entity.getUuid());

        if (!publicFlag) {
            menuDto.setPublicFlag(EnumUtil.covertCodeboardEnum(YN.class, entity.getPublicFlag()));

            menuDto.setRegDate(entity.getRegDate());
            menuDto.setModDate(entity.getModDate());
            menuDto.setModDate(entity.getModDate());
        }

        // Lazy하게 되어있으므로 따로 호출안함.
//        menuDto.setChildrenList(entity.get);

        return menuDto;
    }

    private MenuEntity getParentMenu(MenuDto menuDto, MemberDto memberDto) {
        MenuDto checkParent = new MenuDto();
        checkParent.setSeq(menuDto.getParentSeq());
        return getMenu(checkParent, memberDto);
    }


}
