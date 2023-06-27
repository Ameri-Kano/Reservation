package com.amerikano.reservation.entity.dto;

import com.amerikano.reservation.entity.Review;
import lombok.*;

import java.time.LocalDateTime;

/**
 * 리뷰 정보 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewDto {

    private Long id;
    private Long customerId;
    private String title;
    private String detail;
    private Double rate;

    private LocalDateTime createdAt;
    private LocalDateTime lastModifiedAt;

    /**
     * Entity -> DTO
     */
    public static ReviewDto from(Review review) {
        return ReviewDto.builder()
            .id(review.getId())
            .customerId(review.getCustomerId())
            .title(review.getTitle())
            .detail(review.getDetail())
            .rate(review.getRate())
            .createdAt(review.getCreatedAt())
            .lastModifiedAt(review.getLastModifiedAt())
            .build();
    }
}
