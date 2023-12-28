package com.hhs.codeboard.blog.data.entity.board.entity;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.data.entity.common.entity.DefaultDateEntity;
import com.hhs.codeboard.blog.data.entity.common.entity.DefaultEntity;
import com.hhs.codeboard.blog.enumeration.EditorType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

import java.io.Serial;

@Getter
@Setter
@Entity
@Table(name="code_board_article_content")
@Where(clause = "del_date is null")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class BoardArticleContentEntity extends DefaultDateEntity {

    @Id
    @Column
    @GeneratedValue(strategy= GenerationType.TABLE, generator = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR)
    private Long seq;

//    @Column
//    private Long articleSeq;

    @Column(nullable = false)
    private String editor;

    @Column(nullable = false, columnDefinition = "MEDIUMTEXT")
    private String content;

    @Column(nullable = false)
    private Long contentOrder;

    @Column
    private String langType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "article_seq", updatable = false)
    private BoardArticleEntity article;

}
