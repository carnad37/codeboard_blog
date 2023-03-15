package com.hhs.codeboard.blog.jpa.entity.common.dto;

import lombok.Data;

import jakarta.persistence.Column;
import java.time.LocalDateTime;

@Data
public class DefaultDateDto {

    private LocalDateTime regDate;

    private LocalDateTime modDate;

    private LocalDateTime delDate;

}
