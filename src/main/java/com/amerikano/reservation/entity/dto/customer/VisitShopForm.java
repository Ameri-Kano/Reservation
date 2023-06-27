package com.amerikano.reservation.entity.dto.customer;

import lombok.*;

/**
 * 고객 방문 확인을 위한 DTO
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VisitShopForm {
    private String name;
    private String phone;
}
