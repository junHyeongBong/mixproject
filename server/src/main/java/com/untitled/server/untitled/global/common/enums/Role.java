package com.untitled.server.untitled.global.common.enums;

import org.springframework.util.ObjectUtils;

import java.util.Optional;

public enum Role {
    ADMIN,  //관리자
    USER;   //사용자

    public static Optional<Role> of(String name) {
        if(ObjectUtils.isEmpty(name)) {
            return Optional.empty();
        }

        try {
            return Optional.of(Role.valueOf(name.toUpperCase()));
        } catch (IllegalArgumentException e) {  //잘못된 인수값이 메소드에 전달될때
            return Optional.empty();
        }

    }

    //TEST시
    //System.out.println(Role.of("admin")) : 출력 : ADMIN
    //Role.of("USER") 출력 : USER
    //Role.of("unknown") 출력 : null
    //Role.of(null) 출력 : null


}
