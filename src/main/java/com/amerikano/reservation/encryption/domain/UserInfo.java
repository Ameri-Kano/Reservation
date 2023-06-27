package com.amerikano.reservation.encryption.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 토큰을 통해 얻을 수 있는 회원 정보(고유값인 id, 이메일)
 */
@Getter
@AllArgsConstructor
public class UserInfo {

    private Long id;
    private String email;
}
