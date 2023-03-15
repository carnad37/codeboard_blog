package com.hhs.codeboard.blog.jpa.entity.board.entity;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.jpa.entity.board.BoardCateArticleEntity;
import com.hhs.codeboard.blog.jpa.entity.common.entity.DefaultEntity;
import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import java.util.Collection;

@Data
@Entity
@Table(name="code_board_article")
@Where(clause = "del_date is null")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class BoardArticleEntity extends DefaultEntity{

    private static final long serialVersionUID = 536546113468443744L;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String summary;

    @Column
    private String publicFlag;
    
    @Column
    private Integer boardSeq;

    @Column
    @Nullable
    private Integer categorySeq;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="articleSeq")
    private Collection<BoardCateArticleEntity> categorys;

}
