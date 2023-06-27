package com.amerikano.reservation.entity.dto.reservation;

import com.amerikano.reservation.entity.Reservation;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * 예약 정보 DTO
 * <p>
 * 정보 : 매장 id, 예약 식별 코드, 예약 날짜, 예약 시간(enum), 방문 시간, 예약 승인/방문/삭제 여부
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReservationDto {
    private String code;
    private Long shopId;

    private LocalDate reservedDate;
    private String reserveTime;

    private LocalDateTime visitedAt;

    private Boolean accepted;
    private Boolean visited;
    private Boolean canceled;

    /**
     * Entity -> DTO
     */
    public static ReservationDto from(Reservation reservation) {
        return ReservationDto.builder()
            .code(reservation.getCode())
            .shopId(reservation.getShop().getId())
            .reservedDate(reservation.getReservedDate())
            .reserveTime(reservation.getReserveTime().getTimeString())
            .visitedAt(reservation.getVisitedAt())
            .accepted(reservation.getAccepted())
            .visited(reservation.getVisited())
            .canceled(reservation.getCanceled())
            .build();
    }
}
