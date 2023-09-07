package com.hhs.codeboard.blog.data.entity.board.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.hhs.codeboard.blog.data.entity.common.dto.DataFormatDto;
import com.hhs.codeboard.blog.data.entity.common.dto.DefaultSearchDto;
import com.hhs.codeboard.blog.enumeration.YN;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import org.springdoc.core.annotations.ParameterObject;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class BoardArticleDto extends DefaultSearchDto {

//    @Parameter swagger 테스트
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

    @JsonProperty(access = JsonProperty.Access.READ_ONLY)
    private List<BoardArticleContentDto> contents;

    @Schema(implementation = DataFormatDto.class)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private DataFormatDto<BoardArticleContentDto> uploadContents;

}
