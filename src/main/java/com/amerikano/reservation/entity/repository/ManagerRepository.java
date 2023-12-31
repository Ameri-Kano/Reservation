package com.amerikano.reservation.entity.repository;

import com.amerikano.reservation.entity.manager.Manager;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repository for manager entity
 */
@Repository
public interface ManagerRepository extends JpaRepository<Manager, Long> {

    Optional<Manager> findByEmail(String email);

    Boolean existsByEmail(String email);
}
