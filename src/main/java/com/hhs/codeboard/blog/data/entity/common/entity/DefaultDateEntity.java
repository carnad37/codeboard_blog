package com.hhs.codeboard.blog.data.entity.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class DefaultDateEntity implements Serializable {

	@Serial
	private static final long serialVersionUID = -7080461491952563362L;

	/**
	 * entity정보
	 */
	@Column
	private LocalDateTime regDate;
	
	@Column
	private LocalDateTime modDate;
	
	@Column
	private LocalDateTime delDate;

}
