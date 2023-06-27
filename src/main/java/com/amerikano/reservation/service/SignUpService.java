package com.amerikano.reservation.service;

import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.Customer;
import com.amerikano.reservation.entity.dto.SignUpDto;
import com.amerikano.reservation.entity.manager.Manager;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.amerikano.reservation.exception.ErrorCode.EMAIL_ALREADY_EXIST;

/**
 * 회원가입(점장, 고객 공통) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class SignUpService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;

    /**
     * 점장 회원가입
     */
    public String signUpManager(SignUpDto signUpDto) {
        // 가입하려는 이메일이 이미 사용중인 경우
        if (managerRepository.existsByEmail(signUpDto.getEmail())) {
            throw new ReservationServiceException(EMAIL_ALREADY_EXIST);
        }

        Manager manager = Manager.from(signUpDto);

        // 패스워드는 암호화하여 저장
        manager.setPassword(CryptoUtil.encrypt(manager.getPassword()));

        managerRepository.save(manager);
        return "점장 회원 가입이 완료되었습니다.";
    }

    /**
     * 고객 회원가입
     */
    public String signUpCustomer(SignUpDto signUpDto) {
        // 가입하려는 이메일이 이미 사용중인 경우
        if (customerRepository.existsByEmail(signUpDto.getEmail())) {
            throw new ReservationServiceException(EMAIL_ALREADY_EXIST);
        }

        Customer customer = Customer.from(signUpDto);

        // 패스워드는 암호화하여 저장
        customer.setPassword(CryptoUtil.encrypt(customer.getPassword()));

        customerRepository.save(customer);
        return "고객 회원 가입이 완료되었습니다.";
    }
}
