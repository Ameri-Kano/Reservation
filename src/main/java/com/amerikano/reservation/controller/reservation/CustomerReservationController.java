package com.amerikano.reservation.controller.reservation;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.entity.dto.customer.ReservationForm;
import com.amerikano.reservation.entity.dto.reservation.ReservationDto;
import com.amerikano.reservation.entity.dto.reservation.UpdateReservationDto;
import com.amerikano.reservation.service.reservation.CustomerReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 고객의 예약 진행 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("customer/reservation")
@RequiredArgsConstructor
public class CustomerReservationController {

    private static final String AUTH_HEADER = "Authorization";
    private final CustomerReservationService reservationService;
    private final JwtAuthService authService;

    /**
     * 예약 정보를 이용하여 예약 등록
     */
    @PostMapping
    public ResponseEntity<ReservationForm.ResponseDto> requestReservation(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody ReservationForm.RequestDto requestDto
    ) {
        return ResponseEntity.ok(
            reservationService.registerReservation(
                authService.getIdFromToken(token),
                requestDto
            )
        );
    }

    /**
     * 고객의 모든 예약 조회
     */
    @GetMapping
    public ResponseEntity<List<ReservationDto>> showReservation(
        @RequestHeader(name = AUTH_HEADER) String token
    ) {
        return ResponseEntity.ok(
            reservationService.getCustomerReservations(authService.getIdFromToken(token))
        );
    }

    /**
     * 예약 수정
     */
    @PatchMapping
    public ResponseEntity<UpdateReservationDto> updateReservation(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody UpdateReservationDto updateReservationDto
    ) {
        return ResponseEntity.ok(
            reservationService.updateReservation(
                authService.getIdFromToken(token),
                updateReservationDto
            )
        );
    }

    /**
     * 예약 취소 (예약 고유 코드 활용)
     */
    @PatchMapping("cancel")
    public ResponseEntity<String> cancelReservation(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam String code
    ) {
        reservationService.cancelReservation(
            authService.getIdFromToken(token), code
        );
        return ResponseEntity.ok("예약 취소가 완료되었습니다.");
    }
}
