package com.amerikano.reservation.entity.dto.reservation;

import lombok.*;

/**
 * 예약 현황 테이블 요소 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TimeTableDto {

    private Long id;
    private Long customerId;
    private String timeString;
}
