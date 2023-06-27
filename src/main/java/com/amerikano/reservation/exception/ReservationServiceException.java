package com.amerikano.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예외 처리를 위한 공통 exception
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ReservationServiceException extends RuntimeException {

    private ErrorCode errorCode;
    private String message;

    public ReservationServiceException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.message = errorCode.getDescription();
    }
}
