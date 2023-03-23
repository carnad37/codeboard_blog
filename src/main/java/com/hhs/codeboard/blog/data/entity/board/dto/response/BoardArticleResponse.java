package com.hhs.codeboard.blog.data.entity.board.dto.response;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDto;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BoardArticleResponse extends DefaultDto {
    private String title;
    private String content;
    private String summary;
    private String publicFlag;
    private Integer boardSeq;
    private Integer categorySeq;
}
