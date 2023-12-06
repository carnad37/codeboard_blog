package com.hhs.codeboard.blog.data.entity.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class FileDto extends DefaultDto{

    private String orgFileName;

    private String savFileName;

    private String fileType;

    private Integer typeSeq;

    private Long fileSize;

    private String mime;

    private String ext;

}
