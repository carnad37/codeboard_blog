package com.hhs.codeboard.blog.data.repository;

import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleDAO extends JpaRepository<BoardArticleEntity, Long>, JpaSpecificationExecutor<BoardArticleEntity> {

    //카테고리 값으로 검색
    Optional<BoardArticleEntity> findBySeqAndRegUserSeq(Long seq, Long userSeq);

    //카테고리 값으로 검색
    List<BoardArticleEntity> findAllByCategorySeq(Integer categorySeq);

    @Query("select bae from BoardArticleEntity bae")
    List<BoardArticleEntity> findAllByBoardSeqWithDel();
}
