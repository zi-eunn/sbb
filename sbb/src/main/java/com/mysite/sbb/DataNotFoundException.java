package com.mysite.sbb;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/*
 * 아래의 예외가 발생하면 스프링 부트는 설정된 HTTP 상태 코드와 이유를 포함한
 * 응답을 생성하여 클라이언트에게 반환하게 됨. 여기서는 404 오류 반환*/

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "entity not found")
public class DataNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public DataNotFoundException(String message) {
        super(message);
    }
}