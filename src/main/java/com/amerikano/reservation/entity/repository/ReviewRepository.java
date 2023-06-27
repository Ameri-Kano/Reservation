package com.amerikano.reservation.entity.repository;

import com.amerikano.reservation.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {

    Page<Review> findAllByCustomerIdAndDeletedIsFalseOrderByLastModifiedAtDesc(Long customerId, Pageable pageable);

    Optional<Review> findByReservationCode(String reservationCode);
}
