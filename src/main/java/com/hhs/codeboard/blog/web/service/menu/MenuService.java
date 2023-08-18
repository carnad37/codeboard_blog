package com.hhs.codeboard.blog.web.service.menu;

import com.hhs.codeboard.blog.config.except.CodeboardException;
import com.hhs.codeboard.blog.config.except.CodeboardParameterException;
import com.hhs.codeboard.blog.config.except.NotFoundDataException;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.QMenuEntity;
import com.hhs.codeboard.blog.enumeration.MenuSeqEnum;
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
import java.util.regex.Pattern;

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
        } else {
            menuDto.setRegUserSeq(null);
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

        Predicate[] wheres = getDefaultConditionToArray(menuDto);

        JPAQuery<MenuEntity> resultList = jpaQueryFactory.selectFrom(menu)
            .leftJoin(menu.childrenList, joinMenu)
            .fetchJoin()
            .where(wheres)
            .distinct();


        return QueryUtil.getPageTreeMapping(resultList , target->mapMenu(target, publicFlag));

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

        if (menuDto.getParentSeq() < 1) {
            // root인경우
            // 부모값 세팅
            menuDto.setParentSeq(MenuSeqEnum.ROOT_MENU.getMenuSeq());
        } else {
            // 부모가 본인 게시판인지 확인
            MenuDto checkParent = new MenuDto();
            checkParent.setSeq(menuDto.getParentSeq());
            if (Objects.isNull(getMenu(checkParent, memberDto))) throw new CodeboardParameterException("잘못된 접근입니다");
        }

        MenuEntity insert = new MenuEntity();
        insert.setTitle(menuDto.getTitle());
        insert.setMenuType(menuDto.getMenuType().getCode());
        insert.setPublicFlag(menuDto.getPublicFlag().getCode());
        insert.setMenuOrder(menuDto.getMenuOrder());
        insert.setParentSeq(menuDto.getParentSeq());
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


//    /**
//     * 메뉴 초기화
//     * @param memberDto
//     * @param request
//     * @return
//     */
//    public List<MenuVO> initMenuList(MemberDto memberDto, HttpServletRequest request) {
//
//        List<MenuEntity> dbMenuList = menuDAO.findAllByRegUserSeqAndDelDateIsNull(memberDto.getUserSeq(), Sort.by(Sort.Direction.ASC, "menuOrder"));
//
//        List<MenuVO> menuList = new ArrayList<>();
//        List<MenuVO> setInnerList = new ArrayList<>();
//
//        //기본메뉴
//        MenuVO constMenuVO = new MenuVO();
//
//        //공통 설정
//        MenuDto setMenu = new MenuDto();
//        setMenu.setSeq(0L);
//        setMenu.setTitle("공통메뉴");
//        setMenu.setMenuType(MenuTypeEnum.S);
//        constMenuVO.setMenu(setMenu);
//
//        //공통게시판 내용추가
//        setMenu = new MenuDto();
//        setMenu.setSeq(0L);
//        setMenu.setTitle("게시판 목록");
//        setMenu.setMenuType(MenuTypeEnum.D);
//        setInnerList.add(new MenuVO(setMenu));
//
//        setMenu = new MenuDto();
//        setMenu.setSeq(0L);
//        setMenu.setTitle("메뉴 목록");
//        setMenu.setMenuType(MenuTypeEnum.U);
//        setInnerList.add(new MenuVO(setMenu));
////        setInnerList.add(new MenuVO(new MenuEntity(0, "카테고리 설정", MenuTypeEnum.CATEGORY_CONFIG.getMenuType())));
//
//        constMenuVO.setChildrenMenu(setInnerList);
//        menuList.add(constMenuVO);
//
//        //메뉴 맵 UUID::VO
//        Map<Long, MenuVO> menuMap = new HashMap<>();
//        List<MenuVO> addTopList = new ArrayList<>();
//
//        //부모자식 구분하기
//        //게시판 같은경우 기본적으론 게시판 공통 메뉴 하위에 들어간다.
//        //게시판 공통 메뉴 같은 경우 하위에 게시판이 있어야지만 활성화된다.
//        for (MenuEntity dbMenu : dbMenuList) {
//            MenuDto menuDto = modelMapper.map(dbMenu, MenuDto.class);
//            MenuVO menuVO = new MenuVO(menuDto);
//            if (menuMap.containsKey(menuVO.getSeq())) {
//                MenuVO targetVO = menuMap.get(menuVO.getSeq());
//                menuVO.setChildrenMenu(targetVO.getChildrenMenu());
//            }
//            menuMap.put(menuVO.getSeq(), menuVO);
//            if (menuVO.getParentSeq() != null && menuVO.getParentSeq() > 0) {
//                //부모값이 있는경우
//                //부모메뉴가 menuMap에 없을경우 임시값 생성
//                MenuVO parentVO = menuMap.computeIfAbsent(menuVO.getParentSeq(), key -> new MenuVO());
//                //childrenList 초기화
//                List<MenuVO> childrenList = parentVO.getChildrenMenu() == null ? new ArrayList<>() : parentVO.getChildrenMenu();
//                childrenList.add(menuVO);
//                parentVO.setChildrenMenu(childrenList);
//            } else {
//                //최상위 메뉴
//                addTopList.add(menuVO);
//            }
//        }
//
//        MenuVO topMenu = new MenuVO();
//        topMenu.setChildrenMenu(addTopList);
//        menuList.addAll(addTopList);
//
//        SessionUtil.setSession(request, "menuList", menuList);
//        SessionUtil.setSession(request, "menuMap", menuMap);
//        SessionUtil.setSession(request, "maxDepth", getMaxDepth(topMenu, 0, 0));
//
//        return menuList;
//    }

    /**
     * maxDept 얻기용 재귀 주회
     * @param targetVO
     * @param maxDepth
     * @return
     */
//    private int getMaxDepth(MenuVO targetVO, int selfDepth, int maxDepth) {
//        /**
//         * selfDepth는 실제 해당 메뉴의 depth
//         * maxDepth는 전체 주회돌면서 찾아야하는 maxDepth
//         * maxDepth로 setDepth를 해주게되면 점점 depth가 늘어나는 오류가있어서
//         * selfDepth값을 따로 둠으로서 해결.
//         * 주회 종결시에는 selfDepth를 리턴해서 그동안 전달되온 maxDepth와 크기비교.
//         * 모든 비교가 끝나면 검색점을 형제 메뉴로 옮기면서 maxDepth 전달.
//         */
//        //depth 설정
//        targetVO.setDepth(selfDepth);
//
//        if (targetVO.getChildrenMenu() == null) {
//            //재귀 종료
//            return selfDepth;
//        } else {
//            //재귀 진행
//            selfDepth += 1;
//        }
//
//        //목록내에서 가장 큰 depth를 리턴
//        for (MenuVO childrenVO : targetVO.getChildrenMenu()) {
//            childrenVO.setDepth(selfDepth);
//            int depth = getMaxDepth(childrenVO, selfDepth, maxDepth);
//            maxDepth = Math.max(depth, maxDepth);
//        }
//
//        return maxDepth;
//    }

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
        return Arrays.asList(
                QueryUtil.longNullable(menuDto.getSeq(), menu.seq::eq),
                QueryUtil.longNullable(menuDto.getUserSeq(), menu.regUserSeq::eq),
                menuDto.getParentSeq() != null ? menu.parentSeq.eq(menuDto.getParentSeq()) : null,
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
        menuDto.setParentSeq(entity.getParentSeq());

        menuDto.setMenuType(EnumUtil.covertCodeboardEnum(MenuTypeEnum.class, entity.getMenuType()));
        menuDto.setMenuOrder(entity.getMenuOrder());
        menuDto.setUuid(entity.getUuid());

        if (!publicFlag) {
            menuDto.setPublicFlag(EnumUtil.covertCodeboardEnum(YN.class, entity.getPublicFlag()));

            menuDto.setRegDate(entity.getRegDate());
            menuDto.setModDate(entity.getModDate());
            menuDto.setModDate(entity.getModDate());
        }


//        menuDto.setChildrenMenu(entity.get);

        return menuDto;
    }

//    /**
//     * entity, dto 맵핑용
//     * @param entity
//     * @return
//     */
//    private MenuDto mapMenu(MenuEntity entity) {
//        if (entity == null) return null;
//        return modelMapper.map(entity, MenuDto.class);
//    }


}
