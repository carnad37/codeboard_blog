package com.hhs.codeboard.blog.data.entity.common.dto;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Schema(description = "데이터 업로드")
@JsonAutoDetect
public class  DataFormatDto<T> {

    private List<T> update;
    private List<T> insert;
    private List<T> delete;

}
