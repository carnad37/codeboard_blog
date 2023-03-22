package com.hhs.codeboard.blog.web.controller.prv;

import com.hhs.codeboard.blog.jpa.entity.board.dto.request.BoardArticleRequest;
import com.hhs.codeboard.blog.jpa.entity.board.dto.response.BoardArticleResponse;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 해당 기능은 member module과 통신이 앞단에서 필요하도록 개발
 * Filter로 사전에 통신후 유저 정보를 파라미터로 넘겨받게 한다.
 * Spring security 로 할지여부는 좀더 고민.
 */
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
    public ResponseEntity<List<BoardArticleResponse>> find(BoardArticleRequest boardArticleRequest) {
        return ResponseEntity.ok(articleService.selectArticleList(boardArticleRequest));
    }

    @PostMapping("/save")
    public ResponseEntity<BoardArticleResponse> save(BoardArticleRequest request) {
        return ResponseEntity.ok(articleService.saveArticle(request));
    }


}
