package com.amerikano.reservation.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

/**
 * 리뷰 엔티티
 * <p>
 * 정보 : 해당 매장 ID, 예약 코드(중복 리뷰 방지), 제목, 상세, 평점
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long customerId;

    private String reservationCode;

    private Long shopId;

    private String title;
    private String detail;
    private Double rate;

    private Boolean deleted;
}
