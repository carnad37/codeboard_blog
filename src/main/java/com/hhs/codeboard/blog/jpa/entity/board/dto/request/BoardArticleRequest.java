package com.hhs.codeboard.blog.jpa.entity.board.dto.request;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class BoardArticleRequest {

    

    @Parameter
    private Integer seq;
    private String title;
    private String content;
    private String summary;
    private String publicFlag;
    private Integer boardSeq;
    private Integer categorySeq;
    private LocalDate searchStartDate;
    private LocalDate searchEndDate;
}
