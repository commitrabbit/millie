package com.millie.common.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum ErrorCode implements BaseErrorCode {

    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 오류가 발생했습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "요청한 리소스를 찾을 수 없습니다."),

    NOT_FOUND_ORDER(HttpStatus.NOT_FOUND, "주문을 찾을 수 없습니다."),

    INVALID_ARGUMENT(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),
    NOT_FOUND_SETTLEMENT_REQUEST(HttpStatus.NOT_FOUND, "정산 요청을 찾을 수 없습니다."),
    INVALID_SETTLEMENT_REQUEST(HttpStatus.BAD_REQUEST, "정산 요청이 유효하지 않습니다.");

    @Getter
    private final HttpStatus httpStatus;

    private final String message;

    @Override
    public String getCode() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
