package com.hhs.codeboard.blog.web.controller.prv;

import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleDto;
import com.hhs.codeboard.blog.data.entity.board.dto.BoardArticleResponse;
import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.data.entity.common.dto.FileDto;
import com.hhs.codeboard.blog.web.service.board.BoardArticleService;
import com.hhs.codeboard.blog.web.service.common.FileService;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 해당 기능은 member module과 통신이 앞단에서 필요하도록 개발
 * Filter로 사전에 통신후 유저 정보를 파라미터로 넘겨받게 한다.
 * Spring security 로 할지여부는 좀더 고민.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@RequestMapping("/private/article")
public class PrivateArticleController {

    private final BoardArticleService articleService;
    private final MenuService menuService;
    @PostMapping("/saveImage")
    public ResponseEntity<CommonResponse<FileDto>> saveImage(BoardArticleDto request) throws Exception {
        FileDto result = articleService.saveImage(request.getUploadFile());
        CommonResponse<FileDto> response = new CommonResponse<>(result);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<BoardArticleDto>> save(@ParameterObject  @RequestBody BoardArticleDto request) {
        BoardArticleDto article = articleService.saveArticle(request);
        CommonResponse<BoardArticleDto> response = new CommonResponse<>(article);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonResponse<BoardArticleDto>> delete(@ParameterObject  @RequestBody BoardArticleDto request) {
        BoardArticleDto article = articleService.deleteArticle(request);
        CommonResponse<BoardArticleDto> response = new CommonResponse<>(article);
        return ResponseEntity.ok(response);
    }


}
