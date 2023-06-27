package com.amerikano.reservation.entity.manager;

import com.amerikano.reservation.entity.BaseEntity;
import com.amerikano.reservation.entity.Review;
import com.amerikano.reservation.entity.dto.shop.RegisterShopDto;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

/**
 * 매장 엔티티
 * <p>
 * 정보 : 점장 ID, 매장 이름, 종류(타입), 설명, 총 평점, 등록 리뷰
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Shop extends BaseEntity {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long managerId;
    private String name;
    private String type;
    private String description;
    private String phone;

    @Column(columnDefinition = "double default 0.0")
    private Double rate;

    @OneToMany
    private List<Review> reviews;

    public static Shop of(Long managerId, RegisterShopDto registerShopDto) {
        return Shop.builder()
            .managerId(managerId)
            .name(registerShopDto.getName())
            .type(registerShopDto.getType())
            .description(registerShopDto.getDescription())
            .phone(registerShopDto.getPhone())
            .rate(0.0)
            .reviews(new ArrayList<>())
            .build();
    }
}
