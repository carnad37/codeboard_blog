package com.hhs.codeboard.blog.data.entity.board.dto;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class BoardArticleResponse implements Serializable {

    private int totalPage = 0;

    private long totalCnt = 0;

    private List<BoardArticleDto> articleList;

}
