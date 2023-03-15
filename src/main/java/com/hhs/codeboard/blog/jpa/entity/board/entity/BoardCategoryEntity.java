package com.hhs.codeboard.blog.jpa.entity.board.entity;

import jakarta.persistence.*;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.jpa.entity.common.entity.DefaultEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name="code_category")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class BoardCategoryEntity extends DefaultEntity {

    private static final long serialVersionUID = -6177068167629590168L;

    @Column
    private String title;

    @Column
    private String useF;

    @Column
    private Integer boardSeq;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="menuSeq")
//    private MenuEntity menu;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name="categorySeq")
//    private Collection<BoardCateArticleEntity> categorys;

}
