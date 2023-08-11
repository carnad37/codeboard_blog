package com.hhs.codeboard.blog.data.entity.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;


@Getter
@Setter
public class DefaultSearchDto extends DefaultDto{

    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private int pageIndex = 1;
    @JsonIgnore
    private int contentSize = 10;
    @JsonIgnore
    private int pageSize = 10;
    @JsonIgnore
    private Long userSeq;

    @JsonIgnore
    public long getOffset() {
        return (long) Math.max(pageIndex, 0) * contentSize;
    }

}
