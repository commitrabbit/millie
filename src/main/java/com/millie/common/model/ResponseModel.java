package com.millie.common.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.millie.common.exception.ErrorCode;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ResponseModel<T> {

	private boolean success;

	private T response;

	private Error error;

	public static <T> ResponseModel<T> success() {
		return new ResponseModel<>(true, null, null);
	}

	public static <T> ResponseModel<T> success(T data) {
		return new ResponseModel<>(true, data, null);
	}

	public static ResponseModel<Void> error(Error error) {
		return new ResponseModel<>(false, null, error);
	}

	public record Error(ErrorCode code, String message, List<Map<String, Object>> reason) {
	}
}