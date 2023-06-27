package com.amerikano.reservation.entity.repository;

import com.amerikano.reservation.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository for customer entity
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
}
