//package com.hhs.codeboard.blog.config.aop;
//
//import com.hhs.codeboard.blog.config.anno.AspectMenuActive;
//import com.hhs.codeboard.blog.enumeration.MenuTypeEnum;
//
//import org.springframework.context.annotation.EnableAspectJAutoProxy;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.context.request.RequestContextHolder;
//import org.springframework.web.context.request.ServletRequestAttributes;
//import org.springframework.web.servlet.HandlerMapping;
//
//import jakarta.servlet.http.HttpServletRequest;
//import java.util.Map;
//
//@Component
//@Aspect
//@EnableAspectJAutoProxy
//public class AspectMenuConfig {
//
//    @Around("@annotation(com.hhs.codeboard.config.anno.AspectMenuActive)")
//    public Object menuActive (ProceedingJoinPoint pjp) throws Throwable {
//        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
//        MethodSignature methodSignature = (MethodSignature) pjp.getSignature();
//        AspectMenuActive menuAnno = methodSignature.getMethod().getAnnotation(AspectMenuActive.class);
//        MenuTypeEnum targetEnum = menuAnno.menuType();
//        SessionUtil.setSession(request, "viewMenuType", targetEnum.getMenuType());
//
//        //기본적으로 현재 메뉴가 유저가 등록한 컨텐츠일때만 uuid를 부여
//        //그 외에는 null로 초기화
//        if (MenuTypeEnum.MENU.equals(targetEnum) || MenuTypeEnum.BOARD.equals(targetEnum)) {
//            Map<String, String> pathVariable = (Map<String, String>)request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
//            SessionUtil.setSession(request, "viewMenuUUID", pathVariable.get("uuid"));
//        } else {
//            SessionUtil.setSession(request, "viewMenuUUID", null);
//        }
//
//        //메뉴 타이틀 부여
//        String menuTitle = menuAnno.menuTitle();a
//        if (StringUtils.hasText(menuTitle)) {
//            SessionUtil.setSession(request, "menuTitle", menuTitle);
//        }
//
//        return pjp.proceed(pjp.getArgs());
//    }
//
//}
