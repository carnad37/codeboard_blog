package com.hhs.codeboard.blog.data.entity.common.entity;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import jakarta.persistence.*;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@MappedSuperclass
public abstract class DefaultEntity extends DefaultDateEntity {

	@Serial
	private static final long serialVersionUID = 3989969904364677147L;

	@Id
	@Column(nullable = false, updatable = false)
	@GeneratedValue(strategy= GenerationType.TABLE, generator = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR)
	private Long seq;

	@Column(nullable = false, updatable = false)
	private Long regUserSeq;

	@Column
	private Long modUserSeq;

}
