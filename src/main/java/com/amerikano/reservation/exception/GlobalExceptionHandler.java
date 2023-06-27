package com.amerikano.reservation.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * 예외 처리 핸들러
 */
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ReservationServiceException.class)
    public ResponseEntity<ErrorResponse> handleServiceException(ReservationServiceException e) {
        return ResponseEntity.badRequest()
            .body(new ErrorResponse(e.getErrorCode(), e.getMessage()));
    }
}
