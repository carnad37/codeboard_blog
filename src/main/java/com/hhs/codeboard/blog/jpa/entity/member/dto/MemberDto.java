package com.hhs.codeboard.blog.jpa.entity.member.dto;

import com.hhs.codeboard.blog.jpa.entity.common.dto.DefaultDateDto;
import com.hhs.codeboard.blog.jpa.entity.menu.entity.MenuEntity;

import java.util.Collection;

public class MemberDto extends DefaultDateDto {

    private String email;

    private String password;

    private String nickName;

    private String userType;

    private Integer modUserSeq;

    private Collection<MenuEntity> menuList;

}
