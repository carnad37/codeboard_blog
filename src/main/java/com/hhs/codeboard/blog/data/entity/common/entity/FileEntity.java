package com.hhs.codeboard.blog.data.entity.common.entity;

import com.hhs.codeboard.blog.config.common.CommonStaticProperty;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.TableGenerator;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;

/**
 * 파일 테이블
 * typeSeq는 각 파일의 대응하는 컨텐츠및 기능에 대앙하는 구분값인데,
 * 해당값은 컨텐츠 저장이 완료되면 그떄 저장된다.
 * (컨텐츠 저장시, seq값이랑 같이 전송)
 * 만약 컨텐츠 저장에 성공하면 typeSeq값이 해당 컨텐츠 값으로 저장된다.
 */
@Getter
@Setter
@Entity
@Table(name="code_board_file")
@Where(clause = "del_date is null")
@TableGenerator(name = CommonStaticProperty.SEQUENCE_TABLE_GENERATOR, table = CommonStaticProperty.SEQUENCE_TABLE_NAME)
public class FileEntity extends DefaultEntity {

    @Column(nullable = false)
    private String orgFileName;

    @Column(nullable = false)
    private String savFileName;

    @Column(nullable = false)
    private String fileType;

    @Column
    private Integer typeSeq;

    @Column(nullable = false)
    private Integer fileSize;

    @Column(nullable = false)
    private String mime;

    @Column(nullable = false)
    private String ext;

}
