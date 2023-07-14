package com.hhs.codeboard.blog.data.entity.board.entity;

import com.hhs.codeboard.blog.enumeration.YN;
import jakarta.persistence.*;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.data.entity.common.entity.DefaultEntity;

import lombok.Getter;
import lombok.Setter;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name="code_category")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class BoardCategoryEntity extends DefaultEntity {

    @Serial
    private static final long serialVersionUID = -6177068167629590168L;

    @Column
    private String title;

    @Column
    private YN useF;

    @Column
    private Long boardSeq;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name="menuSeq")
//    private MenuEntity menu;
//
//    @OneToMany(fetch = FetchType.LAZY)
//    @JoinColumn(name="categorySeq")
//    private Collection<BoardCateArticleEntity> categorys;

}
