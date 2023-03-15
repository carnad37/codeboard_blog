package com.hhs.codeboard.blog.jpa.entity.member.entity;

import java.util.Collection;

import jakarta.persistence.*;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.jpa.entity.common.entity.DefaultDateEntity;
import com.hhs.codeboard.blog.jpa.entity.menu.entity.MenuEntity;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Entity
@Table(name = "code_member")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class MemberEntity extends DefaultDateEntity {

	private static final long serialVersionUID = -3781361636538961523L;

	@Id
	@GeneratedValue(strategy=GenerationType.TABLE, generator = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR)
	private Integer seq;
	
	@Column
	private String email;

	@Column
	private String password;
	
	@Column
	private String nickName;
	
	@Column
	private String userType;

	@Column
	private Integer modUserSeq;

	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinColumn(name="regUserSeq")
	private Collection<MenuEntity> menuList;

//	@OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
//	@JoinTable(name="menu",
//		joinColumns = @JoinColumn(name="regUserSeq"),
//		inverseJoinColumns = @JoinColumn(name="menuSeq")
//	)
//	private Collection<MenuEntity> menuList;

}