package com.amerikano.reservation.service;

import com.amerikano.reservation.encryption.domain.UserType;
import com.amerikano.reservation.encryption.service.JwtAuthService;
import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.dto.SignInDto;
import com.amerikano.reservation.entity.manager.Manager;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SignInService {

    private final ManagerRepository managerRepository;
    private final JwtAuthService authService;

    public String getManagerToken(SignInDto signInDto) {
        Manager manager = managerRepository.findByEmailAndPassword(
                signInDto.getEmail(), CryptoUtil.encrypt(signInDto.getPassword()))
            .orElseThrow(() -> new RuntimeException("정보에 해당하는 유저를 찾을 수 없습니다."));

        return authService.createToken(manager.getId(), manager.getEmail(), UserType.MANAGER);
    }

    public String getCustomerToken(SignInDto signInDto) {
        Manager manager = managerRepository.findByEmailAndPassword(
                signInDto.getEmail(), CryptoUtil.encrypt(signInDto.getPassword()))
            .orElseThrow(() -> new RuntimeException("정보에 해당하는 유저를 찾을 수 없습니다."));

        return authService.createToken(manager.getId(), manager.getEmail(), UserType.CUSTOMER);
    }
}
