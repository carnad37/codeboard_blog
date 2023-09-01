package com.hhs.codeboard.blog.data.entity.menu.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.hhs.codeboard.blog.config.anno.TreeMappingKey;
import com.hhs.codeboard.blog.config.anno.TreeMappingTarget;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDto;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
import com.hhs.codeboard.blog.enumeration.SecurityAuthType;
import com.hhs.codeboard.blog.enumeration.YN;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
@Schema(description = "메뉴")
public class MenuDto extends DefaultSearchDto {

    @Schema(description = "메뉴명")
    private String title;

    @Schema(description = "메뉴 순서")
    private Integer menuOrder;

    @Schema(description = "상위 메뉴 구분자")
//    @TreeMappingKey(rootValue = "0")
    private Long parentSeq;

    @Schema(description = "메뉴 UUID")
    private String uuid;

    @Schema(description = "메뉴 타입")
    private MenuTypeEnum menuType;

    @Schema(description = "공개여부")
    /** 블로그 탭으로 사용여부 */
    private YN publicFlag;


    /**
     * 해당 메뉴가 Board타입일때만 호출
     * 그냥 호출해도 상관없긴한데, 하위값은 없음.
     */
    @Schema(description = "게시물 리스트")
    private List<BoardArticleEntity> articleList;

    @Schema(description = "하위 메뉴리스트")
//    @TreeMappingTarget
    private List<MenuDto> childrenList;

    @JsonIgnore
    private MenuEntity parent;
}
