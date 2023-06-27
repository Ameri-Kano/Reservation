package com.amerikano.reservation.entity.repository;

import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.type.ReserveTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

/**
 * Repository for reservation entity
 */
@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {

    List<Reservation> findAllByCustomerId(Long customerId);

    List<Reservation> findAllByShopIdOrderByReservedDate(Long shopId);

    List<Reservation> findAllByShopIdAndReservedDate(Long shopId, LocalDate reservedDate);

    Optional<Reservation> findByCode(String code);

    boolean existsByShopIdAndReservedDateAndReserveTime
        (Long shopId, LocalDate date, ReserveTime reserveTime);
}
