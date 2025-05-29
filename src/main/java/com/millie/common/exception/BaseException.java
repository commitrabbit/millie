package com.millie.common.exception;

import io.micrometer.common.util.StringUtils;
import lombok.Getter;
import org.springframework.web.server.ResponseStatusException;

public class BaseException extends ResponseStatusException {

	@Getter
	private final ErrorCode errorCode;

	@Getter
	private final String errorMessage;

	public BaseException(ErrorCode errorCode) {
		super(errorCode.getHttpStatus(), errorCode.getMessage());
		this.errorCode = errorCode;
		errorMessage = null;
	}

	public BaseException(ErrorCode errorCode, String errorMessage) {
		super(errorCode.getHttpStatus(), errorCode.getMessage());
		this.errorCode = errorCode;
		this.errorMessage = errorMessage;
	}

	@Override
	public String getMessage() {
		if (StringUtils.isEmpty(errorMessage)) {
			return errorCode.getMessage();
		}
		return String.format("%s [%s]", errorCode.getMessage(), errorMessage);
	}
}