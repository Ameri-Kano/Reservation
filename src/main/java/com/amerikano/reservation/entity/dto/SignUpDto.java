package com.amerikano.reservation.entity.dto;

import lombok.*;

/**
 * 회원가입 정보 객체(점장, 고객 공통)
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignUpDto {

    private String email;
    private String name;
    private String password;
    private String phone;
}
