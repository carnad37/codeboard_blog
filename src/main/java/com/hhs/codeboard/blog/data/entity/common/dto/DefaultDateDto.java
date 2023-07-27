package com.hhs.codeboard.blog.data.entity.common.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class DefaultDateDto {

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime delDate;

}
