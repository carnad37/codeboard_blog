package com.hhs.codeboard.blog.data.entity.menu.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.common.entity.DefaultEntity;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.enumeration.YN;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serial;
import java.util.Collection;


@Getter
@Setter
@Entity
@Table(name = "code_menu")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class MenuEntity extends DefaultEntity {

    @Serial
    private static final long serialVersionUID = 6217465463857529869L;

    /*
     * 유저별로 게시판이 생성됨.
     * 생성한 유저 정보가 곧 게시판 주인. (regUser)
     * 메뉴분기를 따로안두고(메뉴 => 게시판), 보드 매니저랑 카테고리만 연동.
     * cate <-> board table에서 얻은 cateList 필요.
     * cateList로 boardArticle을 검색.
     *
     * Board랑 Menu의 차이? 타입만 두면 안되나?
     * Menu의 타입 : board, menu
     * Board일 경우 : 외부에 출력되는 탭(메뉴)일지 체크
     */
    @Column
    private String title;

    @Column
    private Integer menuOrder;

    @Column
    private MenuTypeEnum menuType;

    @Column
    private Long parentSeq;

    @Column
    private YN publicFlag;

    @Column
    private String uuid;
//
//    /**
//     * 해당 메뉴가 Board타입일때만 호출
//     * 그냥 호출해도 상관없긴한데, 하위값은 없음.
//     */
    @JsonIgnore
    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="boardSeq")
    @Where(clause = "del_date is null")
    private Collection<BoardArticleEntity> articleList;

}
