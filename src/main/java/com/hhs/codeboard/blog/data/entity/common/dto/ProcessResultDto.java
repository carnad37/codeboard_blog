package com.hhs.codeboard.blog.data.entity.common.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProcessResultDto {

    /* 메세지 */
    private String result;
    /* 구분자 */
    private Long seq;
    /* 성공여부 */
    private boolean success;

}
