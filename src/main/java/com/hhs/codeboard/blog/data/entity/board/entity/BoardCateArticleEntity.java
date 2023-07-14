//package com.hhs.codeboard.blog.data.entity.board.entity;
//
//import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
//import com.hhs.codeboard.blog.data.entity.board.entity.BoardCateItemEntity;
//import jakarta.persistence.*;
//import lombok.Getter;
//import lombok.Setter;
//
//import java.io.Serial;
//import java.io.Serializable;
//
//@Getter
//@Setter
//@Embeddable
//@Entity
//@Table(name="connect_category_article")
//@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
//public class BoardCateArticleEntity implements Serializable{
//
//    @Serial
//    private static final long serialVersionUID = -701957800132578608L;
//
//    @Id
//    @ManyToOne(fetch = FetchType.EAGER)
//    @JoinColumn(name = "cateItemSeq")
//    private BoardCateItemEntity category;
//
//
//}