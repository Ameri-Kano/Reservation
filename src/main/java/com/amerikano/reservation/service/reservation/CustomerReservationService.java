package com.amerikano.reservation.service.reservation;

import com.amerikano.reservation.entity.Customer;
import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.entity.dto.customer.VisitShopForm;
import com.amerikano.reservation.entity.dto.reservation.ReservationDto;
import com.amerikano.reservation.entity.dto.reservation.UpdateReservationDto;
import com.amerikano.reservation.entity.manager.Shop;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import com.amerikano.reservation.type.ReserveTime;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
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
     * 예약 정보를 바탕으로 예약 진행
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

        checkAlreadyExist(
            shop.getId(), requestDto.getReservedDate(), requestDto.getReserveTime()
        );

        if (checkReservedDateTime(requestDto.getReservedDate(), requestDto.getReserveTime())) {
            throw new ReservationServiceException(RESERVATION_DATETIME_OVER);
        }

        // 예약 정보 생성 후 저장
        // 예약 승인은 기본값을 null 로 설정(점장 미확인)
        Reservation newReservation = getNewReservation(customerId, shop, requestDto);

        reservationRepository.save(newReservation);

        return ResponseDto.builder()
            .shopId(shop.getId())
            .code(newReservation.getCode())
            .reservedDate(newReservation.getReservedDate())
            .reserveTime(newReservation.getReserveTime().getTimeString())
            .build();
    }

    /**
     * 새로운 예약 정보 생성 후 저장
     * <p>
     * 예약 승인(accepted)은 기본값을 null 로 설정(점장 미확인 상태)
     */
    private Reservation getNewReservation(Long customerId, Shop shop, RequestDto requestDto) {
        return Reservation.builder()
            .customerId(customerId)
            .shop(shop)
            .code(getRandomCode())
            .reservedDate(requestDto.getReservedDate())
            .reserveTime(requestDto.getReserveTime())
            .accepted(null)
            .visited(false)
            .canceled(false)
            .build();
    }

    /**
     * 해당 고객의 모든 예약 조회
     */
    public List<ReservationDto> getCustomerReservations(Long customerId) {
        return reservationRepository.findAllByCustomerId(customerId)
            .stream()
            .map(ReservationDto::from)
            .collect(Collectors.toList());
    }

    /**
     * 예약 정보 변경
     */
    @Transactional
    public UpdateReservationDto updateReservation(Long customerId, UpdateReservationDto updateDto) {
        // 코드에 해당하는 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByCode(updateDto.getCode())
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        validateReservationCommon(reservation);

        // 예약 고객 정보가 일치하지 않는 경우
        matchCustomerId(customerId, reservation);

        // 변경된 정보가 없는 경우
        if (reservation.getReservedDate().equals(updateDto.getReservedDate()) &&
            reservation.getReserveTime().equals(updateDto.getReserveTime())
        ) {
            throw new ReservationServiceException(RESERVATION_NO_CHANGE);
        }

        checkAlreadyExist(
            reservation.getShop().getId(), updateDto.getReservedDate(), updateDto.getReserveTime()
        );

        if (checkReservedDateTime(updateDto.getReservedDate(), updateDto.getReserveTime())) {
            throw new ReservationServiceException(RESERVATION_DATETIME_OVER);
        }

        reservation.setReservedDate(updateDto.getReservedDate());
        reservation.setReserveTime(updateDto.getReserveTime());

        return updateDto;
    }

    /**
     * 예약 정보 취소
     */
    @Transactional
    public void cancelReservation(Long customerId, String code) {
        // 코드에 해당하는 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        validateReservationCommon(reservation);

        matchCustomerId(customerId, reservation);

        reservation.setCanceled(true);
    }

    /**
     * 예약한 매장 방문 처리
     */
    @Transactional
    public String visitReservedShop(String code, VisitShopForm form) {
        // 코드에 해당하는 예약이 존재하는지 확인
        Reservation reservation = reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));

        Customer customer = customerRepository.findById(reservation.getCustomerId())
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_CUSTOMER_NOT_EXIST));

        validateReservationCommon(reservation);

        checkVisitForm(customer, form);

        // 예약을 확인하지 않았다면 방문할 수 없음
        if (reservation.getAccepted() == null) {
            throw new ReservationServiceException(RESERVATION_NOT_PROCESSED);
        }

        // 시간을 지나버렸으면 방문 불가 및 동시에 취소 처리
        if (checkVisitDateTimeOver(reservation.getReservedDate(),
            reservation.getReserveTime()
        )) {
            reservation.setCanceled(true);
            throw new ReservationServiceException(VISIT_DATETIME_OVER);
        }

        // 너무 일찍 방문하려 하는 경우
        if (checkVisitDateTimeEarly(reservation.getReservedDate(),
            reservation.getReserveTime()
        )) {
            throw new ReservationServiceException(VISIT_DATETIME_EARLY);
        }

        reservation.setVisited(true);
        reservation.setVisitedAt(LocalDateTime.now());

        return "예약 방문 처리가 완료되었습니다";
    }

    /**
     * 예약의 기본 검증
     */
    private void validateReservationCommon(Reservation reservation) {
        // 이미 취소된 예약인 경우
        if (reservation.getCanceled()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_CANCELED);
        }

        // 이미 완료 처리된 예약인 경우
        if (reservation.getVisited()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_VISITED);
        }

        // 이미 거절된 예약인 경우
        if (reservation.getAccepted() != null &&
            !reservation.getAccepted()) {
            throw new ReservationServiceException(RESERVATION_ALREADY_REFUSED);
        }
    }

    /**
     * 예약의 날짜와 시간 체크
     */
    private boolean checkReservedDateTime(LocalDate date, ReserveTime reserveTime) {
        // 예약 날짜와 시간을 조합하여 현재 시간과 비교
        LocalDateTime dateTime = date.atTime(
            Integer.parseInt(reserveTime.getTimeString()),
            0
        );

        // 현재 시간을 지났으면 예약 불가
        return LocalDateTime.now().isAfter(dateTime);
    }

    /**
     * 예약한 고객과 요청한 고객이 같은지 확인
     */
    private void matchCustomerId(Long customerId, Reservation reservation) {
        if (!Objects.equals(reservation.getCustomerId(), customerId)) {
            throw new ReservationServiceException(RESERVATION_WRONG_CUSTOMER);
        }
    }

    /**
     * 전달받은 시점(날짜와 시간 조합)에 예약이 이미 존재하는지 체크
     */
    private void checkAlreadyExist(Long shopId, LocalDate date, ReserveTime reserveTime) {
        if (reservationRepository.existsByShopIdAndReservedDateAndReserveTime(
            shopId, date, reserveTime
        )) {
            throw new ReservationServiceException(RESERVATION_ALREADY_EXIST);
        }
    }

    /**
     * 예약 정보와 방문자가 입력한 정보 체크
     * <p>
     * (두 가지 다 일치해야 방문 확인 가능)
     */
    private void checkVisitForm(Customer customer, VisitShopForm form) {
        if (!customer.getName().equals(form.getName()) ||
            !customer.getPhone().equals(form.getPhone())
        ) {
            throw new ReservationServiceException(WRONG_VISIT_FORM);
        }
    }

    /**
     * 방문한 날짜와 시간 체크
     * <p>
     * (방문 가능 시간은 예약 시간부터 1시간 ~ 10분(0 ~ 50분) 전이라고 설정)
     */
    private boolean checkVisitDateTimeOver(LocalDate date, ReserveTime reserveTime) {
        // 예약 날짜와 시간을 조합하여 현재 시간과 비교
        LocalDateTime dateTime = date.atTime(
            Integer.parseInt(reserveTime.getTimeString()) - 1,
            51
        );

        // 51분을 넘을 시 방문 불가능
        return LocalDateTime.now().isAfter(dateTime);
    }

    private boolean checkVisitDateTimeEarly(LocalDate date, ReserveTime reserveTime) {
        // 예약 날짜와 시간을 조합하여 현재 시간과 비교
        LocalDateTime dateTime = date.atTime(
            Integer.parseInt(reserveTime.getTimeString()) - 1,
            0
        );

        // 0분 이전일시 방문 불가능
        return LocalDateTime.now().isBefore(dateTime);
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
