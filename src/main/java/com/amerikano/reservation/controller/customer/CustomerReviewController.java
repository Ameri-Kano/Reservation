package com.amerikano.reservation.controller.customer;

import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.entity.dto.customer.PostReviewForm;
import com.amerikano.reservation.entity.dto.customer.UpdateReviewForm;
import com.amerikano.reservation.service.customer.CustomerReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * 고객 리뷰 api 컨트롤러
 * <p>
 * 모든 기능은 로그인 확인을 위한 토큰 필요
 */
@RestController
@RequestMapping("customer/review")
@RequiredArgsConstructor
public class CustomerReviewController {

    private static final String AUTH_HEADER = "Authorization";
    private final CustomerReviewService reviewService;
    private final JwtAuthService authService;

    /**
     * 고객의 리뷰 조회
     */
    @GetMapping
    public ResponseEntity<?> searchReview(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam(defaultValue = "1") Integer page
    ) {
        return ResponseEntity.ok(
            reviewService.searchReviews(
                authService.getIdFromToken(token),
                page
            ));
    }

    /**
     * 예약 이용 후 리뷰 작성
     */
    @PostMapping("{code}")
    public ResponseEntity<String> postReview(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody PostReviewForm form,
        @PathVariable String code
    ) {
        return ResponseEntity.ok(
            reviewService.postReview(
                authService.getIdFromToken(token), form, code
            )
        );
    }

    /**
     * 리뷰 수정
     */
    @PutMapping("{code}")
    public ResponseEntity<String> updateReview(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestBody UpdateReviewForm form,
        @PathVariable String code
    ) {
        return ResponseEntity.ok(
            reviewService.updateReview(
                authService.getIdFromToken(token), form, code
            )
        );
    }

    /**
     * 리뷰 삭제
     */
    @DeleteMapping("/delete")
    public ResponseEntity<String> deleteReview(
        @RequestHeader(name = AUTH_HEADER) String token,
        @RequestParam String code
    ) {
        return ResponseEntity.ok(
            reviewService.deleteReview(
                authService.getIdFromToken(token), code
            )
        );
    }

}
