package com.hhs.codeboard.blog.web.controller.prv;

import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.util.service.QueryUtil;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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


    @GetMapping("/findAll")
    public ResponseEntity<CommonResponse<MenuDto>> findAll(@ParameterObject MenuDto request, @AuthenticationPrincipal MemberDto memberInfo) {
        // member 정보 제공
        request.setUserSeq(memberInfo.getUserSeq());

        List<MenuDto> menuList = menuService.selectAll(request, false);

        CommonResponse<MenuDto> response = new CommonResponse<>(menuList);
        return ResponseEntity.ok(response);
    }

    @GetMapping("/find")
    public ResponseEntity<CommonResponse<MenuDto>> find(@ParameterObject MenuDto request, @AuthenticationPrincipal MemberDto memberInfo) {
        // member 정보 제공
        request.setUserSeq(memberInfo.getUserSeq());
        MenuDto menu = menuService.selectOne(request, false);

        CommonResponse<MenuDto> response = new CommonResponse<>(menu);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/save")
    public ResponseEntity<CommonResponse<MenuDto>> saveMenu(@ParameterObject @RequestBody MenuDto request) {
        MenuDto menuDto = menuService.insertMenu(request);
        CommonResponse<MenuDto> response = new CommonResponse<>(menuDto);
        return ResponseEntity.ok(response);
    }

    @PutMapping("/save")
    public ResponseEntity<CommonResponse<Void>> updateMenu(@ParameterObject @RequestBody MenuDto request) {
        menuService.updateMenu(request);
        CommonResponse<Void> response = new CommonResponse<>(HttpStatus.OK.value(), null, "success", false);
        return ResponseEntity.ok(response);
    }
}
