package com.amerikano.reservation.service.manager;

import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import com.amerikano.reservation.type.AcceptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.amerikano.reservation.exception.ErrorCode.*;

/**
 * 예약 관리(예약 승인) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class ReservationAdminService {

    private final ShopRepository shopRepository;
    private final ReservationRepository reservationRepository;

    /**
     * 예약 코드를 이용하여 예약 승인/거절 결정
     */
    @Transactional
    public String acceptOrRefuseReservation(Long managerId, String code, AcceptStatus accepted) {
        // 예약 정보 및 해당하는 매장이 존재하는지 체크
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        checkReservation(reservation);

        if (!shopRepository.existsByIdAndManagerId(
            reservation.getShop().getId(), managerId
        )) {
            throw new ReservationServiceException(SHOP_NOT_EXIST);
        }

        Boolean status = accepted.getStatus();

        // 승인 여부에 따라 결정(승인 or 거절)
        reservation.setAccepted(status);

        String statusString = status ? "승인" : "거절";

        return code + " 예약에 대한 " + statusString + " 처리가 완료되었습니다.";
    }

    /**
     * 예약 검증
     */
    private void checkReservation(Reservation reservation) {
        // 이미 취소된 예약인 경우
        if (reservation.getCanceled()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_CANCELED);
        }

        // 이미 완료 처리된 예약인 경우
        if (reservation.getVisited()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_VISITED);
        }

        // 이미 확인(승인/거절)한 예약인 경우
        if (reservation.getAccepted() != null) {
            throw new ReservationServiceException(RESERVATION_ALREADY_PROCESSED);
        }
    }
}
