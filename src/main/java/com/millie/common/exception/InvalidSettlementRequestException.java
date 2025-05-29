package com.millie.common.exception;

public class InvalidSettlementRequestException extends BaseException {

    public InvalidSettlementRequestException() {
        super(ErrorCode.INVALID_SETTLEMENT_REQUEST);
    }

    public InvalidSettlementRequestException(String message) {
        super(ErrorCode.INVALID_SETTLEMENT_REQUEST, message);
    }
}
