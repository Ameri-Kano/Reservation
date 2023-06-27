package com.amerikano.reservation.entity.dto.shop;

import lombok.*;

/**
 * 매장 정보 수정 DTO
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateShopDto {

    private Long id;
    private String name;
    private String type;
    private String description;
    private String phone;
}