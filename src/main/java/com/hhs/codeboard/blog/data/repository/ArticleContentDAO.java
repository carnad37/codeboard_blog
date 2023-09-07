package com.hhs.codeboard.blog.data.repository;

import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleContentEntity;
import com.hhs.codeboard.blog.data.entity.board.entity.BoardArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArticleContentDAO extends JpaRepository<BoardArticleContentEntity, Long>, JpaSpecificationExecutor<BoardArticleContentEntity> {

}
