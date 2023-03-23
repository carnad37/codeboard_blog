package com.hhs.codeboard.blog.data.entity.common.dto;

import lombok.Data;


@Data
public class DefaultDto extends DefaultDateDto{

    private Integer seq;

    private Integer regUserSeq;

    private Integer modUserSeq;

}
