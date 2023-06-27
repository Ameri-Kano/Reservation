package com.amerikano.reservation.entity.dto.customer;

import com.amerikano.reservation.type.Rate;
import lombok.*;

/**
 * 리뷰 작성 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostReviewForm {

    private String title;
    private String detail;
    private Rate rate;

    /**
     * post form -> update form
     */
    public static UpdateReviewForm to(PostReviewForm form) {
        return UpdateReviewForm.builder()
            .title(form.getTitle())
            .detail(form.getDetail())
            .rate(form.getRate())
            .build();
    }
}
