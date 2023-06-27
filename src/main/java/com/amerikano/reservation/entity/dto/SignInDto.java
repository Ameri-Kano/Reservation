package com.amerikano.reservation.entity.dto;

import lombok.*;

/**
 * 로그인 DTO (점장, 고객 공통)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignInDto {

    private String email;
    private String password;
}
