package com.amerikano.reservation.entity.repository;

import com.amerikano.reservation.entity.manager.Shop;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for shop entity
 */
@Repository
public interface ShopRepository extends JpaRepository<Shop, Long> {

    Optional<Shop> findByIdAndManagerId(Long managerId, Long id);

    Page<Shop> findAllByOrderByRateDesc(Pageable pageable);

    Page<Shop> findAllByOrderByNameAsc(Pageable pageable);

    Page<Shop> findAllByTypeLikeAndNameLikeOrderByNameAsc(String type, String name, Pageable pageable);

}
