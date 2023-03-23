package com.hhs.codeboard.blog.data.entity.common.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class DefaultDateDto {

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime delDate;

}
