package com.amerikano.reservation.service.reservation;

import com.amerikano.reservation.entity.dto.reservation.TimeTableDto;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReservationTableService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;

    public Map<LocalDate, List<TimeTableDto>> getReservationTable(Long shopId) {
        if (!shopRepository.existsById(shopId)) {
            throw new RuntimeException("해당 가게가 존재하지 않습니다.");
        }

        Map<LocalDate, List<TimeTableDto>> reservationTable = new HashMap<>();

        reservationRepository.findAllByShopIdOrderByReservedDateDesc(shopId)
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

    public List<TimeTableDto> getReservationTableByDate(Long shopId, LocalDate date) {
        if (!shopRepository.existsById(shopId)) {
            throw new RuntimeException("해당 가게가 존재하지 않습니다.");
        }

        return reservationRepository.findAllByShopIdAndReservedDate(shopId, date)
            .stream()
            .map(reservation ->
                TimeTableDto.builder()
                    .id(reservation.getId())
                    .customerId(reservation.getCustomerId())
                    .timeString(reservation.getReserveTime().getTimeString())
                    .build())
            .collect(Collectors.toList());
    }
}
