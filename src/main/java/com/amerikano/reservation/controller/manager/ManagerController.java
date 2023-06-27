package com.amerikano.reservation.controller.manager;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.entity.dto.ChangePasswordDto;
import com.amerikano.reservation.entity.dto.manager.UpdateManagerDto;
import com.amerikano.reservation.service.manager.ManagerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 점장 정보 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("manager")
@RequiredArgsConstructor
public class ManagerController {

    private static final String AUTH_HEADER = "Authorization";
    private final ManagerService managerService;
    private final JwtAuthService authService;

    /**
     * 점장 정보 수정
     */
    @PatchMapping
    public ResponseEntity<UpdateManagerDto> updateManager(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody UpdateManagerDto updateManagerDto
    ) {
        return ResponseEntity.ok(
            managerService.updateManager(
                authService.getIdFromToken(token),
                updateManagerDto
            )
        );
    }

    /**
     * 점장 패스워드 수정
     */
    @PatchMapping("password")
    public ResponseEntity<String> changeManagerPassword(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody ChangePasswordDto changePasswordDto
    ) {

        managerService.changeManagerPassword(authService.getIdFromToken(token), changePasswordDto);
        return ResponseEntity.ok("비밀번호 변경이 완료되었습니다.");
    }

    /**
     * 회원 탈퇴(점장)
     */
    @DeleteMapping
    public ResponseEntity<String> deleteManager(
        @RequestHeader(name = AUTH_HEADER) String token
    ) {
        managerService.deleteManager(authService.getIdFromToken(token));
        return ResponseEntity.ok("점장 회원 탈퇴가 완료되었습니다.");
    }
}
