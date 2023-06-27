package com.amerikano.reservation.controller;


import com.amerikano.reservation.entity.dto.SignUpDto;
import com.amerikano.reservation.service.SignUpService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 회원가입 api 컨트롤러
 */
@RestController
@RequestMapping("signup")
@RequiredArgsConstructor
public class SignUpController {

    private final SignUpService signUpService;

    /**
     * 점장 회원가입
     */
    @PostMapping("manager")
    public ResponseEntity<String> signUpManager(
        @RequestBody SignUpDto signUpDto
    ) {

        signUpService.signUpManager(signUpDto);
        return ResponseEntity.ok("점장 회원 가입이 완료되었습니다.");
    }

    /**
     * 고객 회원가입
     */
    @PostMapping("customer")
    public ResponseEntity<String> signUpCustomer(
        @RequestBody SignUpDto signUpDto
    ) {

        signUpService.signUpCustomer(signUpDto);
        return ResponseEntity.ok("고객 회원 가입이 완료되었습니다.");
    }
}
