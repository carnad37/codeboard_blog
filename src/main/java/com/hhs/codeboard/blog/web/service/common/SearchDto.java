package com.hhs.codeboard.blog.web.service.common;

import lombok.Data;

import java.io.Serializable;


@Data
public class SearchDto implements Serializable {

    private static final long serialVersionUID = 6414354082484006148L;

    private Integer pageIndex = 1;

    private String searchKeyword;

    private String searchCondition;

}
