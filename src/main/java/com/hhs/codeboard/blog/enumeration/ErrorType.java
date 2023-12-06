package com.hhs.codeboard.blog.enumeration;

import com.hhs.codeboard.blog.config.except.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.function.Function;

@Getter
@RequiredArgsConstructor
public enum ErrorType {

    SUCCESS("성공", "00000", null),

    // 10000~ 요청관련 에러
    NOT_FOUND_DATA("데이터를 찾을수 없습니다", "10001", NotFoundDataException::new),
    INVALID_PARAMETER("{} 파라미터가 잘못되었습니다.", "10002", RequestException::new),

    // 11000 ~ 파일관련 요청
    FILE_INVALID_NAME("파일명을 확인 할 수 없습니다.", "11001", RequestException::new),
    FILE_INVALID_EXT("잘못된 파일 확장자 입니다.", "11002", RequestException::new),
    FILE_INVALID_TYPE("잘못된 파일 타입 입니다.", "11002", RequestException::new),
    FAIL_ANALYZE_FILE("파일 분석에 실패하였습니다", "11003", RequestException::new),

    // 20000~ 권한 관련 에러
    NOT_LOGIN("로그인이 되어있지 않습니다.", "20001", AuthException::new),
    NOT_ACCESS_RIGHTS("접근권한이 없습니다", "20002", AuthException::new),

    // 30000~ 통신 관련
    API_COMMON_ERROR("통신에 실패하였습니다.", "30001", ApiException::new),
    API_ERROR("{} 데이터 요청에 실패하였습니다.", "30001", ApiException::new),

    // 90000~ 공통 및 특수 에러
    SERVER_ERROR("알 수 없는 오류가 발생했습니다.", "90000", CodeboardException::new);

    private final String message;
    private final String errorCode;
    private final Function<Object[], ? extends CodeboardException> exceptionSupplier;

    public CodeboardException supply () {
        return this.supply(new Object[]{});
    }

    public CodeboardException supply (Object[] messages) {
        return exceptionSupplier.apply(messages);
    }
}
