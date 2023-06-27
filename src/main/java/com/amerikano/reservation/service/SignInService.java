package com.amerikano.reservation.service;

import com.amerikano.reservation.encryption.domain.UserType;
import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.Customer;
import com.amerikano.reservation.entity.dto.SignInDto;
import com.amerikano.reservation.entity.manager.Manager;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.amerikano.reservation.exception.ErrorCode.USER_NOT_EXIST;
import static com.amerikano.reservation.exception.ErrorCode.WRONG_PASSWORD;

/**
 * 로그인(점장, 고객 공통) 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class SignInService {

    private final ManagerRepository managerRepository;
    private final CustomerRepository customerRepository;
    private final JwtAuthService authService;

    /**
     * 점장 로그인(토큰 발급)
     */
    public String getManagerToken(SignInDto signInDto) {
        // 이메일로 해당 유저를 찾을 수 없는 경우
        Manager manager = managerRepository.findByEmail(signInDto.getEmail())
            .orElseThrow(() -> new ReservationServiceException(USER_NOT_EXIST));

        // 비밀번호가 틀린 경우
        if (!signInDto.getPassword().equals(CryptoUtil.decrypt(manager.getPassword()))) {
            throw new ReservationServiceException(WRONG_PASSWORD);
        }

        return authService.createToken(manager.getId(), manager.getEmail(), UserType.MANAGER);
    }

    /**
     * 고객 로그인(토큰 발급)
     */
    public String getCustomerToken(SignInDto signInDto) {
        // 이메일로 해당 유저를 찾을 수 없는 경우
        Customer customer = customerRepository.findByEmail(signInDto.getEmail())
            .orElseThrow(() -> new ReservationServiceException(USER_NOT_EXIST));

        // 비밀번호가 틀린 경우
        if (!signInDto.getPassword().equals(CryptoUtil.decrypt(customer.getPassword()))) {
            throw new ReservationServiceException(WRONG_PASSWORD);
        }

        return authService.createToken(customer.getId(), customer.getEmail(), UserType.CUSTOMER);
    }
}
