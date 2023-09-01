package com.hhs.codeboard.blog.data.entity.common.dto;

import io.swagger.v3.oas.annotations.Parameter;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DefaultDto extends DefaultDateDto{

    @Parameter(name = "구분자")
    private Long seq;

    @Parameter(name = "등록 유저구분자")
    private Long regUserSeq;

    @Parameter(name = "수정 유저구분자")
    private Long modUserSeq;

}
