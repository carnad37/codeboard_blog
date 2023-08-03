package com.hhs.codeboard.blog.data.entity.member.dto;

import com.hhs.codeboard.blog.data.entity.common.dto.DefaultDateDto;
import com.hhs.codeboard.blog.data.entity.menu.entity.MenuEntity;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class MemberDto extends DefaultDateDto {

    private String email;

    private String passwd;

    private String nickname;

    private String userType;

    private Long userSeq;

//    private Collection<MenuEntity> menuList;

}
