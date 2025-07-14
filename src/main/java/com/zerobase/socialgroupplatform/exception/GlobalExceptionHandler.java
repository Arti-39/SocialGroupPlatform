package com.zerobase.socialgroupplatform.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(CustomException.class)
  public ResponseEntity<ErrorResponse> handleCustomException(CustomException ex){
    ErrorCode errorCode = ex.getErrorCode();
    ErrorResponse errorResponse = new ErrorResponse(
        errorCode.getStatus(), errorCode.name(), errorCode.getMessage());

    return new ResponseEntity<>(errorResponse, errorCode.getStatus());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ErrorResponse> handleException(Exception ex){
    ErrorResponse errorResponse = new ErrorResponse(
        HttpStatus.INTERNAL_SERVER_ERROR, ErrorCode.INTERNAL_SERVER_ERROR.getMessage(), ex.getMessage());

    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
  }

  @Getter
  @AllArgsConstructor
  public static class ErrorResponse {
    private HttpStatus status;
    private String code;
    private String message;
  }
}
