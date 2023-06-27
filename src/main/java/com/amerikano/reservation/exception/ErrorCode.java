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
    EMAIL_ALREADY_EXIST("해당 이메일은 이미 사용중입니다."),
    WRONG_PASSWORD("비밀번호가 일치하지 않습니다."),

    WRONG_OLD_PASSWORD("현재 비밀번호가 입력한 비밀번호와 일치하지 않습니다."),
    PASSWORD_NO_CHANGE("현재 비밀번호와 새 비밀번호가 일치합니다."),

    USER_NOT_EXIST("해당 유저가 존재하지 않습니다."),
    SHOP_NOT_EXIST("해당 매장이 존재하지 않습니다."),
    RESERVATION_NOT_EXIST("해당 예약이 존재하지 않습니다."),

    RESERVATION_ALREADY_EXIST("해당 날짜와 시간에 이미 예약이 존재합니다."),
    RESERVATION_DATETIME_OVER("예약 가능 날짜 및 시간을 초과했습니다."),
    RESERVATION_WRONG_CUSTOMER("예약 고객 정보가 일치하지 않습니다."),
    RESERVATION_NO_CHANGE("변경된 예약 정보가 없습니다."),
    RESERVATION_ALREADY_CANCELED("이미 취소 처리된 예약입니다."),
    RESERVATION_ALREADY_VISITED("이미 방문 처리된 예약입니다."),
    RESERVATION_ALREADY_REFUSED("이미 거절 처리된 예약입니다."),
    RESERVATION_ALREADY_PROCESSED("이미 승인/거절 처리된 예약입니다."),
    RESERVATION_NOT_PROCESSED("예약이 아직 승인/거절 처리되지 않았습니다."),

    WRONG_VISIT_FORM("방문자 정보가 일치하지 않습니다."),
    VISIT_DATETIME_OVER("방문 시간이 예약 시간을 초과했습니다."),
    VISIT_DATETIME_EARLY("방문 시간이 예약 시간보다 너무 빠릅니다."),

    REVIEW_ALREADY_EXIST("해당 예약에 대한 리뷰가 이미 존재합니다."),
    CANT_POST_REVIEW("조건을 만족하지 않아 리뷰를 작성할 수 없습니다(리뷰는 가게를 방문해야 작성 가능합니다)."),

    SHOP_MANAGER_NOT_EXIST("해당 매장의 점장 유저 정보가 존재하지 않습니다."),
    RESERVATION_CUSTOMER_NOT_EXIST("해당 예약의 고객 유저 정보가 존재하지 않습니다."),

    WRONG_PAGE_INDEX("잘못된 페이지 번호입니다."),
    ;


    private String description;
}
