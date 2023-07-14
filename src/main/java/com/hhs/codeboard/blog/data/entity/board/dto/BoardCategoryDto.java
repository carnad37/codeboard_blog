package com.hhs.codeboard.blog.data.entity.board.dto;

import com.hhs.codeboard.blog.enumeration.YN;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardCategoryDto {

    private Integer seq;

    private String title;

    private YN useF;

    private Long boardSeq;

}
