package com.amerikano.reservation.service.reservation;

import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.entity.dto.reservation.TimeTableDto;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static com.amerikano.reservation.exception.ErrorCode.SHOP_NOT_EXIST;

/**
 * 예약 현황 (시간)테이블 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class ReservationTableService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    /**
     * 해당 매장의 전체 예약 현황 생성
     */
    public Map<LocalDate, List<TimeTableDto>> getReservationTable(Long shopId) {
        // 해당하는 매장이 존재하지 않을 경우
        if (!shopRepository.existsById(shopId)) {
            throw new ReservationServiceException(SHOP_NOT_EXIST);
        }

        // 전체 테이블은 <날짜, 예약현황> 형태의 HashMap
        Map<LocalDate, List<TimeTableDto>> reservationTable = new HashMap<>();

        reservationRepository.findAllByShopIdOrderByReservedDate(shopId)
            .stream()
            .filter(this::checkReservation)
            .forEach(reservation -> {
                    LocalDate key = reservation.getReservedDate();
                    reservationTable.putIfAbsent(key, new ArrayList<>());

                    reservationTable.get(key).add(
                        TimeTableDto.builder()
                            .id(reservation.getId())
                            .customerId(reservation.getCustomerId())
                            .timeString(reservation.getReserveTime().getTimeString())
                            .build()
                    );
                }
            );

        return reservationTable;
    }

    /**
     * 해당 매장의 특정 날짜의 예약 현황 생성
     * <p>
     * 날짜 형식은 yyyy-MM-dd (예: 2023-01-01)
     */
    public List<TimeTableDto> getReservationTableByDate(Long shopId, LocalDate date) {
        // 해당하는 매장이 존재하지 않을 경우
        if (!shopRepository.existsById(shopId)) {
            throw new ReservationServiceException(SHOP_NOT_EXIST);
        }

        return reservationRepository.findAllByShopIdAndReservedDate(shopId, date)
            .stream()
            .filter(this::checkReservation)
            .map(reservation ->
                TimeTableDto.builder()
                    .id(reservation.getId())
                    .customerId(reservation.getCustomerId())
                    .timeString(reservation.getReserveTime().getTimeString())
                    .build())
            .collect(Collectors.toList());
    }

    /**
     * 자리를 차지하고 있는 예약인지 확인(승인되었으며, 취소되지 않은)
     */
    private boolean checkReservation(Reservation reservation) {
        return reservation.getAccepted() != null &&
            reservation.getAccepted() &&
            !reservation.getCanceled();
    }
}
