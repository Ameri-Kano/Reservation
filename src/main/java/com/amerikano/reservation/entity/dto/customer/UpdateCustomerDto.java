package com.amerikano.reservation.entity.dto.customer;

import com.amerikano.reservation.entity.Customer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 고객 정보 수정 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCustomerDto {

    private String email;
    private String name;
    private String phone;

    // Entity -> DTO
    public static UpdateCustomerDto from(Customer customer) {
        return UpdateCustomerDto.builder()
            .email(customer.getEmail())
            .name(customer.getName())
            .phone(customer.getPhone())
            .build();
    }
}
