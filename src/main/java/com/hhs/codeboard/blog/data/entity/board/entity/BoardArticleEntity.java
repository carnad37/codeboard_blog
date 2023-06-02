package com.hhs.codeboard.blog.data.entity.board.entity;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import com.hhs.codeboard.blog.data.entity.common.entity.DefaultEntity;
import com.hhs.codeboard.blog.enumeration.YN;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.lang.Nullable;

import java.io.Serial;
import java.util.Collection;

@Getter
@Setter
@Entity
@Table(name="code_board_article")
@Where(clause = "del_date is null")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class BoardArticleEntity extends DefaultEntity{

    @Serial
    private static final long serialVersionUID = 536546113468443744L;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String summary;

    @Column
    private YN publicFlag;
    
    @Column
    private Integer boardSeq;

    @Column
    private Integer categorySeq;

    @OneToMany(fetch = FetchType.LAZY)
    @JoinColumn(name="articleSeq")
    private Collection<BoardCateArticleEntity> categorys;

}
