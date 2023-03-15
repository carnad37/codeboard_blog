package com.hhs.codeboard.blog.web.controller.prv;

import com.hhs.codeboard.blog.jpa.entity.board.dto.request.BoardArticleRequest;
import com.hhs.codeboard.blog.jpa.entity.board.dto.response.BoardArticleResponse;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@RequestMapping("/private/article")
@Slf4j
@RequiredArgsConstructor
public class PrivateArticleController {

    private final BoardArticleService articleService;

    /**
     * 게시물 조회
     * @param boardArticleRequest
     * @return
     */
    @GetMapping("/find")
    public ResponseEntity<List<BoardArticleResponse>> BoardArticleRequest(BoardArticleRequest boardArticleRequest) {
        return ResponseEntity.ok(articleService.selectArticleList(boardArticleRequest));
    }


}
