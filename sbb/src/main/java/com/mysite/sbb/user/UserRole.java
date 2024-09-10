package com.mysite.sbb.user;

import lombok.Getter;

@Getter //ADMIN과 USER 상수는 값을 변경할 필요가 없기 때문에 setter 없음
public enum UserRole {
    /*
    * enum 자료형(열거 자료형)*/
    ADMIN("ROLE_ADMIN"), //관리자를 의미하는 상수생성
    USER("ROLE_USER"); //사용자를 의미하는 상수생성

    UserRole(String value) {
        this.value = value;
    }

    private String value;
}
