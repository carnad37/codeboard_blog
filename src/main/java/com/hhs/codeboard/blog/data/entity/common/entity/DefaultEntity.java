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
	@Column
	@GeneratedValue(strategy= GenerationType.TABLE, generator = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR)
	private Integer seq;

	@Column
	private Integer regUserSeq;

	@Column
	private Integer modUserSeq;

}
