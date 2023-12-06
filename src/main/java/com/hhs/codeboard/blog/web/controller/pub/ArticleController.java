package com.hhs.codeboard.blog.web.controller.pub;

import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleResponse;
import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.enumeration.FileType;
import com.hhs.codeboard.blog.enumeration.YN;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import com.hhs.codeboard.blog.web.service.common.FileService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


@RestController
@RequestMapping("/public/article")
@Slf4j
@RequiredArgsConstructor
public class ArticleController {

    private final BoardArticleService articleService;
    private final FileService fileService;

    /**
     * 게시물 단건 조회
     */
    @GetMapping("/findAll")
    public ResponseEntity<CommonResponse<BoardArticleResponse>> findAll(@ParameterObject BoardArticleDto boardArticleRequest) {
        BoardArticleResponse response = articleService.selectArticleList(boardArticleRequest);
        return ResponseEntity.ok(new CommonResponse<>(response));
    }

    /**
     * 게시물 조회
     */
    @GetMapping("/find")
    public ResponseEntity<CommonResponse<BoardArticleDto>> find(@ParameterObject BoardArticleDto boardArticleRequest) {
        CommonResponse<BoardArticleDto> response = new CommonResponse<>(articleService.selectArticle(boardArticleRequest.getSeq()));
        return ResponseEntity.ok(response);
    }


}
