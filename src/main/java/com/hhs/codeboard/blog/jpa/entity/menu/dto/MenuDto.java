package com.hhs.codeboard.blog.jpa.entity.menu.dto;

import com.hhs.codeboard.blog.jpa.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.jpa.entity.common.dto.DefaultDto;
import lombok.Data;

import java.util.Collection;

@Data
public class MenuDto extends DefaultDto {

    private String title;

    private Integer menuOrder;

    private String menuType;

    private Integer parentSeq;

    private String uuid;

    /** 블로그 탭으로 사용여부 */
    private String publicF;

    /**
     * 해당 메뉴가 Board타입일때만 호출
     * 그냥 호출해도 상관없긴한데, 하위값은 없음.
     */
    private Collection<BoardArticleEntity> articleList;

}
