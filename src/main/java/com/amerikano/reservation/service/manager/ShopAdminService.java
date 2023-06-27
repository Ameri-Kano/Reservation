package com.amerikano.reservation.service.manager;

import com.amerikano.reservation.entity.dto.shop.RegisterShopDto;
import com.amerikano.reservation.entity.dto.shop.ShopDto;
import com.amerikano.reservation.entity.dto.shop.UpdateShopDto;
import com.amerikano.reservation.entity.manager.Shop;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ErrorCode;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 매장 관리(점장) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class ShopAdminService {

    private final ShopRepository shopRepository;
    private final ManagerRepository managerRepository;

    /**
     * 서비스 처리 전 해당 점장 유저가 존재하는지 확인
     */
    private boolean isManagerExists(Long managerId) {
        return managerRepository.existsById(managerId);
    }

    /**
     * 매장 정보 등록
     */
    public ShopDto registerShop(Long managerId, RegisterShopDto registerShopDto) {
        // 해당 유저가 존재하지 않는 경우
        if (!isManagerExists(managerId)) {
            throw new ReservationServiceException(ErrorCode.SHOP_MANAGER_NOT_EXIST);
        }

        Shop shop = Shop.of(managerId, registerShopDto);
        shopRepository.save(shop);

        return ShopDto.from(shop);
    }

    /**
     * 매장 정보 수정
     */
    @Transactional
    public ShopDto updateShop(Long managerId, UpdateShopDto updateShopDto) {
        // 해당 유저가 존재하지 않는 경우
        if (!isManagerExists(managerId)) {
            throw new ReservationServiceException(ErrorCode.SHOP_MANAGER_NOT_EXIST);
        }

        Shop shop = shopRepository.findByIdAndManagerId(managerId, updateShopDto.getId())
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.SHOP_NOT_EXIST));

        shop.setName(updateShopDto.getName());
        shop.setType(updateShopDto.getType());
        shop.setDescription(updateShopDto.getDescription());

        return ShopDto.from(shop);
    }

    /**
     * 매장 정보 삭제
     */
    @Transactional
    public void deleteShop(Long managerId, Long shopId) {
        // 해당 유저가 존재하지 않는 경우
        if (!isManagerExists(managerId)) {
            throw new ReservationServiceException(ErrorCode.SHOP_MANAGER_NOT_EXIST);
        }

        Shop shop = shopRepository.findByIdAndManagerId(managerId, shopId)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.SHOP_NOT_EXIST));

        shopRepository.delete(shop);
    }
}
