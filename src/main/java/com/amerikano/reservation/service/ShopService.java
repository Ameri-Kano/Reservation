package com.amerikano.reservation.service;

import com.amerikano.reservation.entity.dto.shop.ShopDto;
import com.amerikano.reservation.entity.repository.ShopRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ShopService {

    private final ShopRepository shopRepository;

    public ShopDto showShopById(Long id) {
        return ShopDto.from(
            shopRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("해당 가게가 존재하지 않습니다."))
        );
    }

    // 기준 없이 모든 매장을 보여줌(기본 이름순)
    public List<ShopDto> showAllShops(Integer page) {
        return shopRepository.findAllByOrderByNameAsc(PageRequest.of(page - 1, 10))
            .stream()
            .map(ShopDto::from).collect(Collectors.toList());
    }

    // 모든 매장을 평점 순으로 보여줌
    public List<ShopDto> showAllShopsByRate() {
        return shopRepository.findAllByOrderByRateDesc(PageRequest.of(0, 10))
            .stream()
            .map(ShopDto::from).collect(Collectors.toList());
    }

    // 해당 키워드를 포함하는 매장들을 보여줌
    public List<ShopDto> showShopsByTypeAndKeyword(String type, String keyword) {
        return shopRepository.findAllByTypeLikeAndNameLikeOrderByNameAsc(
                "%" + type + "%",
                "%" + keyword + "%",
                PageRequest.of(0, 10))
            .stream()
            .map(ShopDto::from).collect(Collectors.toList());
    }
}
