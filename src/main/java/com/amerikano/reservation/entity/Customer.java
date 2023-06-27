package com.amerikano.reservation.entity;

import com.amerikano.reservation.entity.dto.SignUpDto;
import jakarta.persistence.*;
import lombok.*;

/**
 * 고객 엔티티
 * <p>
 * 정보 : 이름, 이메일(로그인 아이디), 패스워드, 전화번호
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String email;

    private String name;
    private String password;
    private String phone;

    /**
     * DTO -> Entity
     */
    public static Customer from(SignUpDto signUpDto) {
        return Customer.builder()
            .email(signUpDto.getEmail())
            .name(signUpDto.getName())
            .password(signUpDto.getPassword())
            .phone(signUpDto.getPhone())
            .build();
    }
}

