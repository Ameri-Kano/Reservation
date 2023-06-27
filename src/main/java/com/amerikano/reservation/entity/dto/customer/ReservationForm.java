package com.amerikano.reservation.entity.dto.customer;

import com.amerikano.reservation.type.ReserveTime;
import lombok.*;

import java.time.LocalDate;

/**
 * 고객 예약 등록 DTO
 * <p>요청 : 매장 ID, 예약 일자/시간</p>
 * <p>응답 : 매장 ID, 생성된 예약 코드, 예약 일자/시간</p>
 */
public class ReservationForm {

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class RequestDto {
        private Long shopId;
        private LocalDate reservedDate;
        private ReserveTime reserveTime;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    @Builder
    public static class ResponseDto {
        private Long shopId;
        private String code;
        private LocalDate reservedDate;
        private String reserveTime;
    }
}
