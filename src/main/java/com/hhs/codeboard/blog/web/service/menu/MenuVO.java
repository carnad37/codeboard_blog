package com.hhs.codeboard.blog.web.service.menu;

import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.enumeration.YN;

import java.io.Serializable;
import java.util.List;

/**
 * 내부 vo에서 뽑아옴.
 * Lombok사용안함.
 */
public class MenuVO implements Serializable {

    private static final long serialVersionUID = 9089120878346647881L;

    public MenuVO() {
        super();
    }

    public MenuVO(List<MenuVO> childrenMenu) {
        super();
        this.childrenMenu = childrenMenu;
    }

    public MenuVO(MenuDto menu) {
        super();
        this.menu = menu;
    }

    private MenuVO parentMenu;
    private List<MenuVO> childrenMenu;
    private MenuDto menu;
    private Integer depth;

    public String getTitle() {
        return this.menu.getTitle();
    }

    public void setTitle(String title) {
        this.menu.setTitle(title);
    }

    public Long getSeq() {
        return this.menu.getSeq();
    }

    public void setSeq(Long seq) {
        this.menu.setSeq(seq);
    }

    public MenuVO getParentMenu() {
        return parentMenu;
    }

    public void setParentMenu(MenuVO parentMenu) {
        this.parentMenu = parentMenu;
    }

    public MenuDto getMenu() {
        return menu;
    }

    public void setMenu(MenuDto menu) {
        this.menu = menu;
    }

    public Integer getMenuOrder() {
        return this.menu.getMenuOrder();
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menu.setMenuOrder(menuOrder);
    }

    public Long getParentSeq() {
        return this.menu.getParentSeq();
    }

    public void setParentSeq(Long parentSeq) {
        this.menu.setParentSeq(parentSeq);
    }

    public YN getPublicF() {
        return this.menu.getPublicFlag();
    }

    public void setPublicF(YN publicF) {
        this.menu.setPublicFlag(publicF);
    }

    public String getUuid() {
        return this.menu.getUuid();
    }

    public void setUuid(String uuid) {
        this.menu.setUuid(uuid);
    }

    public MenuTypeEnum getMenuType() {
        return this.menu.getMenuType();
    }

    public void setMenuType(MenuTypeEnum menuType) {
        this.menu.setMenuType(menuType);
    }

    public Integer getDepth() {
        return depth;
    }

    public void setDepth(Integer depth) {
        this.depth = depth;
    }

    public List<MenuVO> getChildrenMenu() {
        return childrenMenu;
    }

    public void setChildrenMenu(List<MenuVO> childrenMenu) {
        this.childrenMenu = childrenMenu;
    }
}
