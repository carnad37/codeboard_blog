package com.hhs.codeboard.blog.web.service.menu;

import com.hhs.codeboard.blog.enumeration.MenuSeqEnum;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.data.repository.MenuDAO;
import com.hhs.codeboard.blog.util.common.SessionUtil;
import com.hhs.codeboard.blog.web.service.member.MemberDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Transactional
@Service
@RequiredArgsConstructor
public class MenuService {

    private final MenuDAO menuDAO;

    private final ModelMapper modelMapper;

    public List<MenuEntity> selectAllBoardMenu(int regUserSeq) {
        List<MenuEntity> resultList = new ArrayList<>();
        List<MenuEntity> menuList = menuDAO.findAllByRegUserSeqAndDelDateIsNull(regUserSeq, Sort.by(Sort.Direction.DESC, "menuOrder"));
//        menuList.forEach(
//                MenuEntity -> resultList.add(new MenuEntity())
//        );
        return resultList;
    }

    public List<MenuEntity> selectMenuList(int regUserSeq, MenuTypeEnum menuType) {
        List<MenuEntity> menuList = menuDAO.findAllByRegUserSeqAndMenuTypeAndDelDateIsNull(regUserSeq, menuType.getMenuType());
        return menuList;
    }

    public MenuEntity selectMenu(int regUserSeq, int menuSeq) throws Exception{
        return menuDAO.findBySeqAndRegUserSeqAndDelDateIsNull(menuSeq, regUserSeq).orElseThrow(()->new Exception("잘못된 접근입니다."));
    }

    public MenuEntity selectMenu(int regUserSeq, String uuid) throws Exception{
//        return menuDAO.findByUuidAndRegUserSeqAndDelDateIsNull(uuid, regUserSeq).orElseThrow(()->new Exception("잘못된 접근입니다."));
        return null;
    }

    public void insertMenu(MenuDto menu, MemberDto memberDto, MenuTypeEnum menuType) throws Exception {
        if (!MenuTypeEnum.BOARD.equals(menuType) && menu.getParentSeq() > 0) {
            //parentSeq 체크, 없으면 exception
            selectMenu(memberDto.getSeq(), menu.getParentSeq());
        } else {
            menu.setParentSeq(MenuSeqEnum.ROOT_MENU.getMenuSeq());
        }

        MenuEntity insert = new MenuEntity();
        insert.setTitle(menu.getTitle());
        insert.setMenuType(menuType.getMenuType());
        insert.setPublicFlag(menu.getPublicF());
        insert.setMenuOrder(menu.getMenuOrder());
        insert.setParentSeq(menu.getParentSeq());
        insert.setRegUserSeq(memberDto.getSeq());
        insert.setRegDate(LocalDateTime.now());
//        insert.setUuid(UUID.randomUUID().toString().replaceAll("-", ""));
        menuDAO.save(insert);
    }

    public void updateMenu(MenuDto menu, MemberDto memberDto, MenuTypeEnum menuType) throws Exception {
        if (!MenuTypeEnum.BOARD.equals(menuType) && menu.getParentSeq() > 0) {
            //parentSeq 체크, 없으면 exception
            selectMenu(memberDto.getSeq(), menu.getParentSeq());
        }

        MenuEntity update = getMenu(menu, memberDto);
        update.setTitle(menu.getTitle());
        update.setModUserSeq(memberDto.getSeq());
        update.setMenuOrder(menu.getMenuOrder());
        update.setParentSeq(menu.getParentSeq());
        update.setPublicFlag(menu.getPublicF());
        update.setModDate(LocalDateTime.now());
        menuDAO.save(update);
    }

    /**
     * 메뉴 삭제(하위메뉴 체크)
     * @param menu
     * @param memberDto
     * @throws Exception
     */
    public void deleteMenu(MenuDto menu, MemberDto memberDto, MenuTypeEnum targetType) throws Exception {
        MenuEntity deleteVO = getMenu(menu, memberDto);
        if (!targetType.getMenuType().equals(deleteVO.getMenuType())) {
            throw new ServiceException("타겟타입이 아닙니다.");
        } else if (targetType.equals(MenuTypeEnum.MENU) && !menuDAO.findAllByParentSeqAndDelDateIsNull(deleteVO.getSeq()).isEmpty()) {
            //메뉴타입일 경우에만 하위메뉴 체크
            throw new ServiceException("하위 메뉴가 있습니다.");
        } else {
            //자식 메뉴가 하나라도 있을경우 삭제불가
            //실제 삭제안하고 delDate만 업데이트
            deleteVO.setDelDate(LocalDateTime.now());
            menuDAO.save(deleteVO);
//            menuDAO.delete(deleteVO);
        }
    }


    /**
     * 메뉴 초기화
     * @param memberDto
     * @param request
     * @return
     */
    public List<MenuVO> initMenuList(MemberDto memberDto, HttpServletRequest request) {

        List<MenuEntity> dbMenuList = menuDAO.findAllByRegUserSeqAndDelDateIsNull(memberDto.getSeq(), Sort.by(Sort.Direction.ASC, "menuOrder"));

        List<MenuVO> menuList = new ArrayList<>();
        List<MenuVO> setInnerList = new ArrayList<>();

        //기본메뉴
        MenuVO constMenuVO = new MenuVO();

        //공통 설정
        MenuDto setMenu = new MenuDto();
        setMenu.setSeq(0);
        setMenu.setTitle("공통메뉴");
        setMenu.setMenuType(MenuTypeEnum.STATIC_MENU.getMenuType());
        constMenuVO.setMenu(setMenu);

        //공통게시판 내용추가
        setMenu = new MenuDto();
        setMenu.setSeq(0);
        setMenu.setTitle("게시판 목록");
        setMenu.setMenuType(MenuTypeEnum.BOARD_CONFIG.getMenuType());
        setInnerList.add(new MenuVO(setMenu));

        setMenu = new MenuDto();
        setMenu.setSeq(0);
        setMenu.setTitle("메뉴 목록");
        setMenu.setMenuType(MenuTypeEnum.MENU_CONFIG.getMenuType());
        setInnerList.add(new MenuVO(setMenu));
//        setInnerList.add(new MenuVO(new MenuEntity(0, "카테고리 설정", MenuTypeEnum.CATEGORY_CONFIG.getMenuType())));

        constMenuVO.setChildrenMenu(setInnerList);
        menuList.add(constMenuVO);

        //메뉴 맵 UUID::VO
        Map<Integer, MenuVO> menuMap = new HashMap<>();
        List<MenuVO> addTopList = new ArrayList<>();

        //부모자식 구분하기
        //게시판 같은경우 기본적으론 게시판 공통 메뉴 하위에 들어간다.
        //게시판 공통 메뉴 같은 경우 하위에 게시판이 있어야지만 활성화된다.
        for (MenuEntity dbMenu : dbMenuList) {
            MenuDto menuDto = modelMapper.map(dbMenu, MenuDto.class);
            MenuVO menuVO = new MenuVO(menuDto);
            if (menuMap.containsKey(menuVO.getSeq())) {
                MenuVO targetVO = menuMap.get(menuVO.getSeq());
                menuVO.setChildrenMenu(targetVO.getChildrenMenu());
            }
            menuMap.put(menuVO.getSeq(), menuVO);
            if (menuVO.getParentSeq() != null && menuVO.getParentSeq() > 0) {
                //부모값이 있는경우
                //부모메뉴가 menuMap에 없을경우 임시값 생성
                MenuVO parentVO = menuMap.computeIfAbsent(menuVO.getParentSeq(), key -> new MenuVO());
                //childrenList 초기화
                List<MenuVO> childrenList = parentVO.getChildrenMenu() == null ? new ArrayList<>() : parentVO.getChildrenMenu();
                childrenList.add(menuVO);
                parentVO.setChildrenMenu(childrenList);
            } else {
                //최상위 메뉴
                addTopList.add(menuVO);
            }
        }

        MenuVO topMenu = new MenuVO();
        topMenu.setChildrenMenu(addTopList);
        menuList.addAll(addTopList);

        SessionUtil.setSession(request, "menuList", menuList);
        SessionUtil.setSession(request, "menuMap", menuMap);
        SessionUtil.setSession(request, "maxDepth", getMaxDepth(topMenu, 0, 0));

        return menuList;
    }

    /**
     * maxDept 얻기용 재귀 주회
     * @param targetVO
     * @param maxDepth
     * @return
     */
    private int getMaxDepth(MenuVO targetVO, int selfDepth, int maxDepth) {
        /**
         * selfDepth는 실제 해당 메뉴의 depth
         * maxDepth는 전체 주회돌면서 찾아야하는 maxDepth
         * maxDepth로 setDepth를 해주게되면 점점 depth가 늘어나는 오류가있어서
         * selfDepth값을 따로 둠으로서 해결.
         * 주회 종결시에는 selfDepth를 리턴해서 그동안 전달되온 maxDepth와 크기비교.
         * 모든 비교가 끝나면 검색점을 형제 메뉴로 옮기면서 maxDepth 전달.
         */
        //depth 설정
        targetVO.setDepth(selfDepth);

        if (targetVO.getChildrenMenu() == null) {
            //재귀 종료
            return selfDepth;
        } else {
            //재귀 진행
            selfDepth += 1;
        }

        //목록내에서 가장 큰 depth를 리턴
        for (MenuVO childrenVO : targetVO.getChildrenMenu()) {
            childrenVO.setDepth(selfDepth);
            int depth = getMaxDepth(childrenVO, selfDepth, maxDepth);
            maxDepth = depth > maxDepth ? depth : maxDepth;
        }

        return maxDepth;
    }

    /**
     * uuid 또는 id로 menuEntity 선택
     * @param menu
     * @param member
     * @return
     * @throws Exception
     */
    private MenuEntity getMenu(MenuDto menu, MemberDto member) throws Exception {
//        return StringUtils.hasText(menu.getUuid()) ?
//                menuDAO.findByUuidAndRegUserSeqAndDelDateIsNull(menu.getUuid(), member.getSeq()).orElseThrow(()->new Exception("잘못된 접근입니다."))
//                : menuDAO.findBySeqAndRegUserSeqAndDelDateIsNull(menu.getSeq(), member.getSeq()).orElseThrow(()->new Exception("잘못된 접근입니다."));
        return null;
    }

    //Deprecated :: 2021-07-18 :: 상위기능이 사용안하게됨
//    /**
//     *
//     * @param targetList
//     * @param delSeqs
//     * @param regUserSeq
//     * @throws Exception
//     */
//    public void txUpdateDepth(List<List<MenuEntity>> targetList, String[] delSeqs, int regUserSeq) throws Exception {
//
//        List<MenuEntity> beforeList = menuDAO.findAllByRegUserSeqAndDelDateIsNull(regUserSeq);
//        //seq로 Map생성
//        Map<Integer, MenuEntity> beforeMap = beforeList.stream()
//                .collect(Collectors.toMap(
//                    MenuEntity::getSeq,
//                    Function.identity()
//                ));
//
//        for(List<MenuEntity> unitList : targetList) {
//            for(MenuEntity target : unitList) {
//                MenuEntity updateEntity = null;
//                if (target.getSeq() > 0) {
//                    //수정된 데이터
//                    updateEntity = beforeMap.get(target.getSeq());
//                    updateEntity.setModDate(LocalDateTime.now());
//                    updateEntity.setModUserSeq(regUserSeq);
//                    updateEntity.setMenuOrder(target.getMenuOrder());
//                    updateEntity.setPublicF(target.getPublicF());
//                    updateEntity.setTitle(target.getTitle());
//                    updateEntity.setParentSeq(target.getParentSeq());
//                } else {
//                    //새로 입력된 데이터
//                    updateEntity = new MenuEntity();
//                    updateEntity.setRegDate(LocalDateTime.now());
//                    updateEntity.setRegUserSeq(regUserSeq);
//                    updateEntity.setMenuOrder(target.getMenuOrder());
//                    updateEntity.setPublicF(target.getPublicF());
//                    updateEntity.setTitle(target.getTitle());
//                    updateEntity.setParentSeq(target.getParentSeq());
//                }
//                menuDAO.save(updateEntity);
//            }
//        }
//
//        //TODO :: 삭제는 차후에 추가
//
//    }

}
