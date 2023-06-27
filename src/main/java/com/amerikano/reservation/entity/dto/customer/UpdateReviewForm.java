package com.amerikano.reservation.entity.dto.customer;

import com.amerikano.reservation.type.Rate;
import lombok.*;

/**
 * 리뷰 수정 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateReviewForm {

    private String title;
    private String detail;
    private Rate rate;
}
