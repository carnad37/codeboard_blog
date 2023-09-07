package com.hhs.codeboard.blog.web.controller.pub;

import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/public/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final BoardArticleService articleService;

//    /**
//     * 게시물 조회
//     */
//    @GetMapping("/findAll")
//    public ResponseEntity<List<BoardArticleDto>> find(@ParameterObject BoardArticleDto boardArticleRequest) {
//        return ResponseEntity.ok(articleService.selectArticleList(boardArticleRequest));
//    }
//
//    /**
//     * 게시물 단건 조회
//     */
//    @GetMapping("/find/{articleSeq}")
//    public ResponseEntity<BoardArticleDto> findOne(@ParameterObject @PathVariable(value = "articleSeq") Long articleSeq) {
//        return ResponseEntity.ok(articleService.selectArticle(articleSeq));
//    }

}
