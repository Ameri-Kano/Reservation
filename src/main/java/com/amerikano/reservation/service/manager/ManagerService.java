package com.amerikano.reservation.service.manager;

import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.dto.ChangePasswordDto;
import com.amerikano.reservation.entity.dto.manager.UpdateManagerDto;
import com.amerikano.reservation.entity.manager.Manager;
import com.amerikano.reservation.entity.repository.ManagerRepository;
import com.amerikano.reservation.exception.ErrorCode;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 점장 정보 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class ManagerService {

    private final ManagerRepository managerRepository;

    /**
     * 점장 정보 변경
     */
    @Transactional
    public UpdateManagerDto updateManager(Long id, UpdateManagerDto updateManagerDto) {
        // 해당 유저가 존재하지 않는 경우
        Manager manager = managerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        manager.setName(updateManagerDto.getName());
        manager.setEmail(updateManagerDto.getEmail());
        manager.setPhone(updateManagerDto.getPhone());

        return UpdateManagerDto.from(manager);
    }

    /**
     * 점장 정보 삭제
     */
    @Transactional
    public void deleteManager(Long id) {
        // 해당 유저가 존재하지 않는 경우
        Manager manager = managerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        managerRepository.delete(manager);
    }

    /**
     * 점장 비밀번호 변경
     */
    @Transactional
    public void changeManagerPassword(Long id, ChangePasswordDto changePasswordDto) {
        // 해당 유저가 존재하지 않는 경우
        Manager manager = managerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        String decryptedPassword = CryptoUtil.decrypt(manager.getPassword());

        // 입력받은 지금 비밀번호가 실제와 일치하지 않는 경우
        if (!decryptedPassword.equals(changePasswordDto.getOldPassword())) {
            throw new ReservationServiceException(ErrorCode.WRONG_OLD_PASSWORD);
        }

        // 지금 비밀번호와 새 비밀번호가 같은 경우
        if (changePasswordDto.getOldPassword()
            .equals(changePasswordDto.getNewPassword())) {
            throw new ReservationServiceException(ErrorCode.PASSWORD_NO_CHANGE);
        }

        manager.setPassword(CryptoUtil.encrypt(changePasswordDto.getNewPassword()));
    }
}
