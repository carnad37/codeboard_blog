package com.hhs.codeboard.blog.data.entity.common.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DefaultDto extends DefaultDateDto{

    private Integer seq;

    private Integer regUserSeq;

    private Integer modUserSeq;

}
