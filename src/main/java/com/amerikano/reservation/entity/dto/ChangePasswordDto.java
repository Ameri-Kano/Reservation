package com.amerikano.reservation.entity.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * 유저 패스워드 변경 DTO (점장, 고객 공통)
 */
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordDto {

    private String oldPassword;
    private String newPassword;
}
