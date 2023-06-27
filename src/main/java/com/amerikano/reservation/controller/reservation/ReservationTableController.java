package com.amerikano.reservation.controller.reservation;

import com.amerikano.reservation.entity.dto.reservation.TimeTableDto;
import com.amerikano.reservation.service.reservation.ReservationTableService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

/**
 * 예약 현황 (시간)테이블 api 컨트롤러
 */
@RestController
@RequestMapping("reservation/table")
@RequiredArgsConstructor
public class ReservationTableController {

    private final ReservationTableService reservationTableService;

    /**
     * 매장의 모든 예약 현황 테이블을 일자별로 조회
     */
    @GetMapping("{id}")
    public ResponseEntity<Map<LocalDate, List<TimeTableDto>>> getTimeTable(@PathVariable Long id) {
        return ResponseEntity.ok(reservationTableService.getReservationTable(id));
    }

    /**
     * 매장의 특정 일자의 예약 현황 테이블 조회
     */
    @GetMapping("{id}/{date}")
    public ResponseEntity<List<TimeTableDto>> getTimeTableByDate(
        @PathVariable Long id,
        @PathVariable LocalDate date
    ) {
        return ResponseEntity.ok(reservationTableService.getReservationTableByDate(id, date));
    }
}
