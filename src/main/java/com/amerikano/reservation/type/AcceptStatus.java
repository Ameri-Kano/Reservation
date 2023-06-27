package com.amerikano.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 예약 승인/거절을 의미하는 상수
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum AcceptStatus {
    ACCEPT(true),
    REFUSE(false);

    Boolean status;
}
