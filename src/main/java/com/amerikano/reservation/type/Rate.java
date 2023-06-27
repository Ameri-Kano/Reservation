package com.amerikano.reservation.type;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 리뷰 평점(1~5)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public enum Rate {
    STAR_1(1.0),
    STAR_2(2.0),
    STAR_3(3.0),
    STAR_4(4.0),
    STAR_5(5.0);

    private Double rateValue;
}
