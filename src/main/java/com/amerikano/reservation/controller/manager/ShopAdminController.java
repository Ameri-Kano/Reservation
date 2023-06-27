package com.amerikano.reservation.controller.manager;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.entity.dto.shop.RegisterShopDto;
import com.amerikano.reservation.entity.dto.shop.ShopDto;
import com.amerikano.reservation.entity.dto.shop.UpdateShopDto;
import com.amerikano.reservation.service.manager.ShopAdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 매장 관리 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("manager/shop")
@RequiredArgsConstructor
public class ShopAdminController {

    private static final String AUTH_HEADER = "Authorization";
    private final ShopAdminService shopAdminService;
    private final JwtAuthService authService;

    /**
     *  매장 등록
     */
    @PostMapping
    public ResponseEntity<ShopDto> registerShop(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody RegisterShopDto registerShopDto
    ) {
        return ResponseEntity.ok(
            shopAdminService.registerShop(
                authService.getIdFromToken(token),
                registerShopDto
            )
        );
    }

    /**
     *  매장 수정
     */
    @PutMapping
    public ResponseEntity<ShopDto> updateShop(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody UpdateShopDto updateShopDto
    ) {
        return ResponseEntity.ok(
            shopAdminService.updateShop(
                authService.getIdFromToken(token),
                updateShopDto
            )
        );
    }

    /**
     *  매장 삭제
     */
    @DeleteMapping
    public ResponseEntity<String> deleteShop(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam Long shopId
    ) {
        shopAdminService.deleteShop(authService.getIdFromToken(token), shopId);
        return ResponseEntity.ok("매장 삭제가 완료되었습니다.");
    }
}
