package com.amerikano.reservation.service.reservation;

import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.entity.dto.reservation.UpdateReservationDto;
import com.amerikano.reservation.entity.manager.Shop;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.amerikano.reservation.entity.dto.customer.ReservationForm.RequestDto;
import static com.amerikano.reservation.entity.dto.customer.ReservationForm.ResponseDto;
import static com.amerikano.reservation.exception.ErrorCode.*;

/**
 * 예약(고객) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class CustomerReservationService {

    private final ReservationRepository reservationRepository;
    private final ShopRepository shopRepository;
    private final CustomerRepository customerRepository;

    /**
     * 서비스 처리 전 해당 고객 유저가 존재하는지 확인
     */
    private boolean isCustomerExists(Long customerId) {
        return customerRepository.existsById(customerId);
    }

    /**
     * 서비스 처리 전 해당 고객 유저가 존재하는지 확인
     */
    public ResponseDto registerReservation(
        Long customerId, RequestDto requestDto
    ) {
        // 해당 유저가 존재하지 않는 경우
        if (!isCustomerExists(customerId)) {
            throw new ReservationServiceException(RESERVATION_CUSTOMER_NOT_EXIST);
        }

        // 해당 매장이 존재하지 않는 경우
        Shop shop = shopRepository.findById(requestDto.getShopId())
            .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST));

        // 이미 그 날짜/시간에 예약이 존재하는 경우
        if (reservationRepository.existsByShopIdAndReservedDateAndReserveTime(
            shop.getId(), requestDto.getReservedDate(), requestDto.getReserveTime()
        )) {
            throw new ReservationServiceException(RESERVATION_ALREADY_EXIST);
        }

        // 예약 날짜와 시간을 조합하여 현재 시간과 비교
        LocalDateTime reserveDateTime = requestDto.getReservedDate()
            .atTime(
                Integer.parseInt(requestDto.getReserveTime().getTimeString()),
                0
            );

        if (LocalDateTime.now().isAfter(reserveDateTime)) {
            throw new ReservationServiceException(RESERVATION_DATETIME_OVER);
        }

        // 예약 정보 생성 후 저장
        // 예약 승인은 기본값을 null 로 설정(점장 미확인)
        Reservation newReservation = Reservation.builder()
            .customerId(customerId)
            .shop(shop)
            .code(getRandomCode())
            .reservedDate(requestDto.getReservedDate())
            .reserveTime(requestDto.getReserveTime())
            .accepted(null)
            .visited(false)
            .canceled(false)
            .build();

        reservationRepository.save(newReservation);

        return ResponseDto.builder()
            .shopId(shop.getId())
            .code(newReservation.getCode())
            .reservedDate(newReservation.getReservedDate())
            .reserveTime(newReservation.getReserveTime().getTimeString())
            .build();
    }

    /**
     * 해당 고객의 모든 예약 조회
     */
    public List<ResponseDto> getCustomerReservations(Long customerId) {
        return reservationRepository.findAllByCustomerId(customerId)
            .stream()
            .map(reservation ->
                ResponseDto.builder()
                    .shopId(reservation.getShop().getId())
                    .code(reservation.getCode())
                    .reservedDate(reservation.getReservedDate())
                    .reserveTime(reservation.getReserveTime().getTimeString())
                    .build()
            )
            .collect(Collectors.toList());
    }

    /**
     * 예약 정보 변경
     */
    @Transactional
    public UpdateReservationDto updateReservation(Long customerId, UpdateReservationDto updateDto) {
        // 코드에 해당하는 예약이 존재하지 않는 경우
        Reservation reservation = reservationRepository.findByCode(updateDto.getCode())
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        // 예약 고객 정보가 일치하지 않는 경우
        if (!Objects.equals(reservation.getCustomerId(), customerId)) {
            throw new ReservationServiceException(RESERVATION_WRONG_CUSTOMER);
        }

        // 변경된 정보가 없는 경우
        if (reservation.getReservedDate().equals(updateDto.getReservedDate()) &&
            reservation.getReserveTime().equals(updateDto.getReserveTime())
        ) {
            throw new ReservationServiceException(RESERVATION_NO_CHANGE);
        }

        // 변경을 원하는 날짜/시간에 예약이 이미 존재하는 경우
        if (reservationRepository.existsByShopIdAndReservedDateAndReserveTime(
            reservation.getShop().getId(), updateDto.getReservedDate(), updateDto.getReserveTime()
        )) {
            throw new ReservationServiceException(RESERVATION_ALREADY_EXIST);
        }

        // 예약 등록과 같은 로직(현재 시간과 예약 날짜+시간 비교)
        LocalDateTime newDateTime = updateDto.getReservedDate()
            .atTime(
                Integer.parseInt(updateDto.getReserveTime().getTimeString()),
                0
            );

        if (LocalDateTime.now().isAfter(newDateTime)) {
            throw new ReservationServiceException(RESERVATION_DATETIME_OVER);
        }

        // TODO: 이미 완료된 예약
//        if (!reservation.isAccepted() || reservation.isDeleted()) {
//            throw new ReservationServiceException();
//        }

        reservation.setReservedDate(updateDto.getReservedDate());
        reservation.setReserveTime(updateDto.getReserveTime());

        return updateDto;
    }

    /**
     * 예약 정보 취소
     */
    @Transactional
    public void cancelReservation(Long customerId, String code) {
        // 코드에 해당하는 예약이 존재하지 않는 경우
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        // 예약 고객 정보가 일치하지 않는 경우
        if (!Objects.equals(reservation.getCustomerId(), customerId)) {
            throw new ReservationServiceException(RESERVATION_WRONG_CUSTOMER);
        }

        // TODO: 이미 완료된 예약
//        if (!reservation.isAccepted() || reservation.isDeleted()) {
//            throw new RuntimeException("거절되었거나 삭제된 예약 정보입니다.");
//        }

        reservation.setCanceled(true);
    }


    /**
     * 예약 코드(문자 숫자 혼합한 10자)를 무작위로 생성하는 메소드
     * <p>
     * (apache.commons.lang3 라이브러리 이용)
     */
    private String getRandomCode() {
        return RandomStringUtils.randomAlphanumeric(10);
    }
}
