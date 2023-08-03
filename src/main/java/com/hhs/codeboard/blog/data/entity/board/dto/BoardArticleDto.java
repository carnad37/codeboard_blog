package com.hhs.codeboard.blog.data.entity.board.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.enumeration.YN;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardArticleDto extends DefaultSearchDto {

//    @Parameter swagger 테스트
    @JsonIgnore
    private Long seq;

    private String title;
    private String content;
    private String summary;
    private YN publicFlag;
    private Long boardSeq;
    private Long categorySeq;

    @JsonIgnore
    private LocalDate searchStartDate;
    @JsonIgnore
    private LocalDate searchEndDate;

}
