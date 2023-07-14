package com.hhs.codeboard.blog.web.controller.prv;

import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/private/menu")
@Slf4j
@RequiredArgsConstructor
public class PrivateMenuController {

    /**
     * 게시판 및 모든 메뉴 구조도가 나온다.
     * 드래그로 변경가능한건 어디까지나 수정시에만.
     * 추가할시엔 모달창으로 입력창이 뜨며 해당 내용을 전부 입력시에만 추가됨.
     * 게시판은 삭제는 불가능하고 이동만 가능.
     */

    private final MenuService menuService;

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<Void>> addMenu(@RequestBody MenuDto request, @AuthenticationPrincipal MemberDto dto) throws Exception {
        MemberDto memberDto = new MemberDto();
        menuService.insertMenu(request, memberDto);

        CommonResponse<Void> response = new CommonResponse<>(HttpStatus.OK.value(), null, "success", false);
        return ResponseEntity.ok(response);
    }

}
