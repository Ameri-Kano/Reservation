package com.amerikano.reservation.entity.dto.manager;

import com.amerikano.reservation.entity.manager.Manager;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 점장 정보 수정 DTO
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateManagerDto {

    private String email;
    private String name;
    private String phone;

    // Entity -> DTO
    public static UpdateManagerDto from(Manager manager) {
        return UpdateManagerDto.builder()
            .email(manager.getEmail())
            .name(manager.getName())
            .phone(manager.getPhone())
            .build();
    }
}
