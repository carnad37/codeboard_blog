package com.hhs.codeboard.blog.data.entity.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Id;
import jakarta.persistence.MappedSuperclass;
import lombok.Data;

@Data
@MappedSuperclass
public abstract class DefaultEntity extends DefaultDateEntity {

	private static final long serialVersionUID = 3989969904364677147L;

	@Id
	@Column
	private Integer seq;

	@Column
	private Integer regUserSeq;

	@Column
	private Integer modUserSeq;

}
