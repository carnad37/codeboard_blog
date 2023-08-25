package com.hhs.codeboard.blog.config.common;

import lombok.experimental.UtilityClass;

/**
 * Spring에선 기본적으로 DI주입으로 힙영역에서 데이터를 보관하며 주고 받음.
 * 그렇기에 어노테이션에서 참조하는 final static 값으로 설정값을 응용하기가 힘듬.
 *
 * 해당 클래스는 혹시나 사용될 static 소스의 공통관리를 위해 사용
 */
@UtilityClass
public class CommonStaticProperty {

    /**
     * 시퀸스는 한테이블에서 일괄적으로 관리될 예정이기에 공통값으로 처리 할 필요가있음.
     */
    public static final  String SEQUENCE_TABLE_NAME = "_sequence";

    /**
     * 제너레이터이름도 공통되게 쓰
     */
    public static final String SEQUENCE_TABLE_GENERATOR = "tableGenerator";


}
