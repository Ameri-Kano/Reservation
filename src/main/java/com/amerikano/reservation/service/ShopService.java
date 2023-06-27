package com.amerikano.reservation.service;

import com.amerikano.reservation.entity.dto.shop.ShopDto;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

import static com.amerikano.reservation.exception.ErrorCode.SHOP_NOT_EXIST;
import static com.amerikano.reservation.exception.ErrorCode.WRONG_PAGE_INDEX;

/**
 * 매장 검색 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    /**
     * 특정 매장의 정보 조회
     */
    public ShopDto showShopById(Long id) {
        return ShopDto.from(
            shopRepository.findById(id)
                .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST))
        );
    }

    /**
     * 매장을 평점 순으로 조회 (10항목씩)
     */
    public List<ShopDto> showAllShopsByRate(Integer page) {
        // 페이지 파라미터가 1보다 작은 경우
        if (page < 1) {
            throw new ReservationServiceException(WRONG_PAGE_INDEX);
        }

        return shopRepository.findAllByOrderByRateDesc(PageRequest.of(page - 1, 10))
            .stream()
            .map(ShopDto::from).collect(Collectors.toList());
    }

    /**
     * 키워드를 만족하는 매장 조회 (10항목씩)
     */
    public List<ShopDto> showShopsByTypeAndKeyword(Integer page, String type, String keyword) {
        if (page < 1) {
            throw new ReservationServiceException(WRONG_PAGE_INDEX);
        }

        return shopRepository.findAllByTypeLikeAndNameLikeOrderByNameAsc(
                "%" + type + "%",
                "%" + keyword + "%",
                PageRequest.of(page - 1, 10))
            .stream()
            .map(ShopDto::from).collect(Collectors.toList());
    }
}
