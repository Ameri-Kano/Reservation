package com.amerikano.reservation.entity.dto.shop;

import lombok.*;

/**
 * 매장 정보 등록 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterShopDto {

    private String name;
    private String type;
    private String description;
    private String phone;
}
