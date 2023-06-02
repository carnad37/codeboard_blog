package com.hhs.codeboard.blog.data.entity.common.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class DefaultSearchDto extends DefaultDto{

    private int pageIndex = 1;
    @JsonIgnore
    private int contentSize = 10;
    @JsonIgnore
    private int pageSize = 10;

    @JsonIgnore
    public long getOffset() {
        return (long) Math.max(pageIndex, 0) * contentSize;
    }

}
