package com.hhs.codeboard.blog.web.service.common;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;

@Getter
@Setter
public class SearchDto implements Serializable {

    private Integer pageIndex = 1;

    private Integer pageSize = 10;

}
