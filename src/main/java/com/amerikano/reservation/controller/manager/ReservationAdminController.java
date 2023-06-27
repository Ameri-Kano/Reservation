package com.amerikano.reservation.controller.manager;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.service.manager.ReservationAdminService;
import com.amerikano.reservation.type.AcceptStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 매장 예약 관리 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("manager/reservation")
@RequiredArgsConstructor
public class ReservationAdminController {

    private static final String AUTH_HEADER = "Authorization";
    private final ReservationAdminService reservationAdminService;
    private final JwtAuthService authService;

    @PatchMapping("/accept")
    public ResponseEntity<String> acceptReservation(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam String code
    ) {
        return ResponseEntity.ok(
            reservationAdminService.acceptOrRefuseReservation(
                authService.getIdFromToken(token),
                code,
                AcceptStatus.ACCEPT
            )
        );
    }

    @PatchMapping("/refuse")
    public ResponseEntity<String> refuseReservation(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam String code
    ) {
        return ResponseEntity.ok(
            reservationAdminService.acceptOrRefuseReservation(
                authService.getIdFromToken(token),
                code,
                AcceptStatus.REFUSE
            )
        );
    }
}
