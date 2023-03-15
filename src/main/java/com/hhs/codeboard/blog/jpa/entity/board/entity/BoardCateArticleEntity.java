package com.hhs.codeboard.blog.jpa.entity.board;

import com.hhs.codeboard.blog.jpa.entity.board.entity.BoardCateItemEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name="connect_category_article")
public class BoardCateArticleEntity implements Serializable{

    private static final long serialVersionUID = -701957800132578608L;

    @Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cateItemSeq")
    private BoardCateItemEntity category;


}