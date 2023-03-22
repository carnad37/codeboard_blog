package com.hhs.codeboard.blog.web.controller.pub;

import com.hhs.codeboard.blog.jpa.entity.board.dto.request.BoardArticleRequest;
import com.hhs.codeboard.blog.jpa.entity.board.dto.response.BoardArticleResponse;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/public/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final BoardArticleService articleService;

    /**
     * 게시물 조회
     * @param boardArticleRequest
     * @return
     */
    @GetMapping("/findAll")
    public ResponseEntity<List<BoardArticleResponse>> find(BoardArticleRequest boardArticleRequest) {
        // 외부 요청은 무조건 공개데이터만 노출
        boardArticleRequest.setPublicFlag("Y");
        return ResponseEntity.ok(articleService.selectArticleList(boardArticleRequest));
    }

//    /**
//     * 게시물 단건 조회
//     * @param articleSeq
//     * @return
//     */
//    @GetMapping("/find/{articleSeq}")
//    public ResponseEntity<BoardArticleResponse> findOne(@PathVariable(value = "articleSeq") Long articleSeq) {
//        boardArticleRequest.setPublicFlag("Y");
//        return ResponseEntity.ok(articleService.selectArticle(articleSeq));
//    }

}
