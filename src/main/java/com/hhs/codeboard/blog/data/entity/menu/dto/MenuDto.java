package com.hhs.codeboard.blog.data.entity.menu.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDto;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.enumeration.YN;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class MenuDto extends DefaultSearchDto {

    private String title;

    private Integer menuOrder;

    private Integer parentSeq;

    private String uuid;

    private MenuTypeEnum menuType;

    /** 블로그 탭으로 사용여부 */
    private YN publicFlag;

    /**
     * 해당 메뉴가 Board타입일때만 호출
     * 그냥 호출해도 상관없긴한데, 하위값은 없음.
     */
    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<BoardArticleEntity> articleList;

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private List<MenuDto> childrenMenu;

}
