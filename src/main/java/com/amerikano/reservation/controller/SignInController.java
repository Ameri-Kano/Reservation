package com.amerikano.reservation.controller;

import com.amerikano.reservation.entity.dto.SignInDto;
import com.amerikano.reservation.service.SignInService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 로그인 api 컨트롤러(로그인으로 JWT 토큰 발급)
 */
@RestController
@RequestMapping("signin")
@RequiredArgsConstructor
public class SignInController {

    private final SignInService signInService;

    /**
     * 점장 로그인
     */
    @PostMapping("manager")
    public ResponseEntity<String> signInManager(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(signInService.getManagerToken(signInDto));
    }

    /**
     * 고객 로그인
     */
    @PostMapping("customer")
    public ResponseEntity<String> signInCustomer(@RequestBody SignInDto signInDto) {
        return ResponseEntity.ok(signInService.getCustomerToken(signInDto));
    }
}
