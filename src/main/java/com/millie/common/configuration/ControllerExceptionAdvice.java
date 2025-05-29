package com.millie.common.configuration;

import com.millie.common.exception.BaseException;
import com.millie.common.exception.ErrorCode;
import com.millie.common.model.ResponseModel;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.*;

@Slf4j
@EnableWebMvc
@ControllerAdvice
public class ControllerExceptionAdvice extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
																  HttpHeaders headers,
																  HttpStatusCode status,
																  WebRequest request) {
		// 순서가 중요할시 LinkedHashMap 으로 변경
		Map<String, List<String>> fieldErrors = new HashMap<>();

		// 필드 단위 에러(FieldError) 처리
		for (FieldError error : ex.getBindingResult().getFieldErrors()) {
			String field = error.getField();
			String errorMessage = error.getDefaultMessage();
			fieldErrors.computeIfAbsent(field, k -> new ArrayList<>()).add(errorMessage);
		}
		// 클래스 단위 에러(ObjectError) 처리
		List<Map<String, Object>> globalErrors = new ArrayList<>();
		for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
			Map<String, Object> reason = new HashMap<>();
			reason.put("field", extractFieldFromArguments(error.getArguments())); // 필드명 추출
			reason.put("errorMessage", error.getDefaultMessage());
			globalErrors.add(reason);
		}

		List<Map<String, Object>> errors = new ArrayList<>();
		fieldErrors.forEach((field, messages) -> {
			Map<String, Object> reason = new HashMap<>();
			reason.put("field", field);
			reason.put("validMessage", messages);
			errors.add(reason);
		});
		errors.addAll(globalErrors);

		return ResponseEntity.status(HttpStatus.OK)
			.body(ResponseModel.error(
				new ResponseModel.Error(ErrorCode.INVALID_ARGUMENT, null, errors)));
	}

	@ExceptionHandler(BaseException.class)
	public ResponseEntity<Object> handleBaseException(BaseException baseException,
													  WebRequest request) {
		String finalMessage;
		if (!StringUtils.isEmpty(baseException.getErrorMessage())) {
			finalMessage = baseException.getErrorMessage();
		} else {
			finalMessage = baseException.getMessage();
		}
		return errorResponse(baseException, baseException.getErrorCode().getHttpStatus(), request,
			baseException.getErrorCode(),
			finalMessage
		);
	}


	/**
	 * ObjectError의 arguments에서 필드명 추출
	 */
	private String extractFieldFromArguments(Object[] arguments) {
		if (arguments == null || arguments.length == 0) {
			return null;
		}
		for (Object arg : arguments) {
			if (arg instanceof String) {
				return (String) arg; // 필드명 반환
			}
		}
		return null;
	}

	/**
	 * 정의하지 않은 API 요청 핸들링
	 */
	@Override
	protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex,
																		 HttpHeaders headers,
																		 HttpStatusCode status,
																		 WebRequest request) {
		return errorResponse(ex, HttpStatus.NOT_FOUND, request, ErrorCode.INVALID_ARGUMENT, "Not Supported Method");
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<Object> handleAllUncaughtException(Exception exception,
															 WebRequest request) {
		return errorResponse(
			exception, HttpStatus.INTERNAL_SERVER_ERROR, request, ErrorCode.INTERNAL_SERVER_ERROR,
			exception.getMessage());
	}

	@Override
	protected ResponseEntity<Object> handleNoHandlerFoundException(NoHandlerFoundException noHandlerFoundException,
																   HttpHeaders headers,
																   HttpStatusCode status,
																   WebRequest request) {
		return errorResponse(
			noHandlerFoundException, HttpStatus.NOT_FOUND, request, ErrorCode.NOT_FOUND,
			noHandlerFoundException.getMessage());
	}

	private ResponseEntity<Object> errorResponse(Exception ex,
												 HttpStatus status,
												 WebRequest request,
												 ErrorCode errorCode,
												 String message) {
		log.error(String.format("[%s] [%s] %s", request.getDescription(true), ex.getClass().getSimpleName(), message));
		return ResponseEntity.status(status)
			.body(ResponseModel.error(new ResponseModel.Error(errorCode, Optional.ofNullable(message).orElse(""), new ArrayList<>())));
	}

	private void addError(List<Map<String, Object>> errors, String field, String errorMessage) {
		String arrayFieldName = field.split("\\[")[0];
		String message = field.contains("[") ? field + " " + errorMessage : errorMessage;

		errors.stream()
			.filter(errorMap -> errorMap.containsKey(arrayFieldName))
			.findFirst()
			.ifPresentOrElse(
				existingError -> ((List<String>) existingError.computeIfAbsent(arrayFieldName, k -> new ArrayList<>())).add(message),
				() -> {
					Map<String, Object> fieldErrorMap = new HashMap<>();
					List<String> messages = new ArrayList<>(List.of(message));
					fieldErrorMap.put(arrayFieldName, messages);
					errors.add(fieldErrorMap);
				});
	}
}