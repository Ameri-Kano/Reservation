package com.amerikano.reservation.entity.dto.reservation;

import com.amerikano.reservation.type.ReserveTime;
import lombok.*;

import java.time.LocalDate;

/**
 * 예약 정보 수정 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReservationDto {
    private String code;
    private LocalDate reservedDate;
    private ReserveTime reserveTime;
}
