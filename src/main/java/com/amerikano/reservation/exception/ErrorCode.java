package com.amerikano.reservation.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 *  에러 코드
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum ErrorCode {
    USER_NOT_EXIST("해당 유저가 존재하지 않습니다."),
    SHOP_NOT_EXIST("해당 매장이 존재하지 않습니다."),
    RESERVATION_NOT_EXIST("해당 예약이 존재하지 않습니다."),

    RESERVATION_ALREADY_EXIST("해당 날짜와 시간에 이미 예약이 존재합니다."),
    RESERVATION_DATETIME_OVER("예약 가능 날짜 및 시간을 초과했습니다."),
    RESERVATION_WRONG_CUSTOMER("예약 고객 정보가 일치하지 않습니다."),
    RESERVATION_NO_CHANGE("변경된 예약 정보가 없습니다."),

    SHOP_MANAGER_NOT_EXIST("해당 매장의 점장 유저 정보가 존재하지 않습니다."),
    RESERVATION_CUSTOMER_NOT_EXIST("해당 예약의 고객 유저 정보가 존재하지 않습니다."),

    WRONG_OLD_PASSWORD("현재 비밀번호가 올바르지 않습니다."),
    PASSWORD_NO_CHANGE("현재 비밀번호와 새 비밀번호가 일치합니다.")
    ;


    private String description;
}
