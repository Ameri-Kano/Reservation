package com.amerikano.reservation.controller.customer;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.entity.dto.ChangePasswordDto;
import com.amerikano.reservation.entity.dto.customer.UpdateCustomerDto;
import com.amerikano.reservation.service.customer.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 고객 정보 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("customer")
@RequiredArgsConstructor
public class CustomerController {

    private static final String AUTH_HEADER = "Authorization";
    private final CustomerService customerService;
    private final JwtAuthService authService;

    /**
     * 고객 정보 수정
     */
    @PatchMapping
    public ResponseEntity<UpdateCustomerDto> updateManager(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody UpdateCustomerDto updateCustomerDto
    ) {
        return ResponseEntity.ok(
            customerService.updateCustomer(
                authService.getIdFromToken(token),
                updateCustomerDto
            )
        );
    }

    /**
     * 고객 패스워드 수정
     */
    @PatchMapping("password")
    public ResponseEntity<String> changeCustomerPassword(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody ChangePasswordDto changePasswordDto
    ) {

        customerService.changeCustomerPassword(authService.getIdFromToken(token), changePasswordDto);
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.");
    }

    /**
     * 회원 탈퇴(고객)
     */
    @DeleteMapping
    public ResponseEntity<String> deleteManager(
        @RequestHeader(name = AUTH_HEADER) String token
    ) {
        customerService.deleteCustomer(authService.getIdFromToken(token));
        return ResponseEntity.ok("고객 회원 탈퇴가 완료되었습니다.");
    }
}
