package com.hhs.codeboard.blog.web.controller.pub;

import com.hhs.codeboard.blog.data.entity.common.dto.CommonResponse;
import com.hhs.codeboard.blog.data.entity.member.dto.MemberDto;
import com.hhs.codeboard.blog.data.entity.menu.dto.MenuDto;
import com.hhs.codeboard.blog.web.service.menu.MenuService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/menu")
@Slf4j
@RequiredArgsConstructor
public class MenuController {

    /**
     * 메뉴 조회용 컨트롤러
     */

    private final MenuService menuService;

    @GetMapping("/findAll")
    public ResponseEntity<CommonResponse<MenuDto>> findAll(MenuDto request) {
        Page<MenuDto> menuList = menuService.selectAll(request);
        CommonResponse<MenuDto> result = new CommonResponse<>(menuList);
        return ResponseEntity.ok(result);
    }

    @GetMapping("/find")
    public ResponseEntity<CommonResponse<MenuDto>> findOne(MenuDto request) {
        MenuDto menu = menuService.selectOne(request);
        CommonResponse<MenuDto> result = new CommonResponse<>(menu);
        return ResponseEntity.ok(result);
    }

}
