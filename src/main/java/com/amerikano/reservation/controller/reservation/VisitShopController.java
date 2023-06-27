package com.amerikano.reservation.controller.reservation;


import com.amerikano.reservation.entity.dto.customer.VisitShopForm;
import com.amerikano.reservation.service.reservation.CustomerReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("reservation/visit")
@RequiredArgsConstructor
public class VisitShopController {

    private final CustomerReservationService reservationService;

    /**
     * 고객이 가게를 방문했을 때 처리
     */
    @PostMapping
    public ResponseEntity<String> visitReservedShop(
        @RequestParam String code,
        @RequestBody VisitShopForm form
    ) {
        return ResponseEntity.ok(
            reservationService.visitReservedShop(code, form)
        );
    }
}
