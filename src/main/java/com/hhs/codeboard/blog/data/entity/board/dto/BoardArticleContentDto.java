package com.hhs.codeboard.blog.data.entity.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.enumeration.EditorType;
import com.hhs.codeboard.blog.enumeration.YN;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Getter
@Setter
public class BoardArticleContentDto implements Serializable {

    private Long seq;
    private Long articleSeq;
    private EditorType editor;
    private String content;
    private Long contentOrder;
    private String langType;

}
