package com.amerikano.reservation.service;

import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.Customer;
import com.amerikano.reservation.entity.dto.SignUpDto;
import com.amerikano.reservation.entity.manager.Manager;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * 공통 회원가입 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;

    /**
     * 점장 회원가입
     */
    public void signUpManager(SignUpDto signUpDto) {
        Manager manager = Manager.from(signUpDto);
        manager.setPassword(CryptoUtil.encrypt(manager.getPassword()));

        managerRepository.save(manager);
    }

    /**
     * 고객 회원가입
     */
    public void signUpCustomer(SignUpDto signUpDto) {
        Customer customer = Customer.from(signUpDto);
        customer.setPassword(CryptoUtil.encrypt(customer.getPassword()));

        customerRepository.save(customer);
    }
}
