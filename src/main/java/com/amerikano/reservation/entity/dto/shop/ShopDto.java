package com.amerikano.reservation.entity.dto.shop;

import com.amerikano.reservation.entity.Review;
import com.amerikano.reservation.entity.dto.ReviewDto;
import com.amerikano.reservation.entity.manager.Shop;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 매장 정보 DTO
 */
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ShopDto {

    private Long id;
    private Long managerId;
    private String name;
    private String type;
    private String description;
    private String phone;
    private Double rate;
    private List<ReviewDto> reviews;

    // Entity -> DTO
    public static ShopDto from(Shop shop) {
        return ShopDto.builder()
            .id(shop.getId())
            .managerId(shop.getManagerId())
            .name(shop.getName())
            .type(shop.getType())
            .description(shop.getDescription())
            .phone(shop.getPhone())
            .rate(shop.getRate())
            .reviews(fromReviews(shop.getReviews()))
            .build();
    }

    /**
     * 매장 조회에 표시되는 리뷰는 최근 5개까지
     */
    private static List<ReviewDto> fromReviews(List<Review> reviews) {
        List<ReviewDto> reviewDtos;

        if (reviews.size() > 5) {
            reviewDtos = reviews.stream()
                .filter(review -> !review.getDeleted())
                .skip(reviews.size() - 5)
                .map(ReviewDto::from)
                .collect(Collectors.toList());
        } else {
            reviewDtos = reviews.stream()
                .filter(review -> !review.getDeleted())
                .map(ReviewDto::from)
                .collect(Collectors.toList());
        }

        Collections.reverse(reviewDtos);
        return reviewDtos;
    }
}
