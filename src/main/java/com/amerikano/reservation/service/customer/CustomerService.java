package com.amerikano.reservation.service.customer;

import com.amerikano.reservation.encryption.util.CryptoUtil;
import com.amerikano.reservation.entity.Customer;
import com.amerikano.reservation.entity.dto.ChangePasswordDto;
import com.amerikano.reservation.entity.dto.customer.UpdateCustomerDto;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.exception.ErrorCode;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * 고객 정보 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class CustomerService {

    private final CustomerRepository customerRepository;

    /**
     * 고객 정보 변경
     */
    @Transactional
    public UpdateCustomerDto updateCustomer(Long id, UpdateCustomerDto updateCustomerDto) {
        // 해당하는 유저가 존재하지 않을경우
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        customer.setName(updateCustomerDto.getName());
        customer.setEmail(updateCustomerDto.getEmail());
        customer.setPhone(updateCustomerDto.getPhone());

        return UpdateCustomerDto.from(customer);
    }

    /**
     * 고객 정보 삭제
     */
    @Transactional
    public void deleteCustomer(Long id) {
        // 해당하는 유저가 존재하지 않을경우
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        customerRepository.delete(customer);
    }

    /**
     * 고객 비밀번호 변경
     */
    @Transactional
    public void changeCustomerPassword(Long id, ChangePasswordDto changePasswordDto) {
        // 해당하는 유저가 존재하지 않을경우
        Customer customer = customerRepository.findById(id)
            .orElseThrow(() -> new ReservationServiceException(ErrorCode.USER_NOT_EXIST));

        String decryptedPassword = CryptoUtil.decrypt(customer.getPassword());

        // 입력받은 지금 비밀번호가 실제와 일치하지 않는 경우
        if (!decryptedPassword.equals(changePasswordDto.getOldPassword())) {
            throw new ReservationServiceException(ErrorCode.WRONG_OLD_PASSWORD);
        }

        // 지금 비밀번호와 새 비밀번호가 같은 경우
        if (changePasswordDto.getOldPassword()
            .equals(changePasswordDto.getNewPassword())) {
            throw new ReservationServiceException(ErrorCode.PASSWORD_NO_CHANGE);
        }

        customer.setPassword(CryptoUtil.encrypt(changePasswordDto.getNewPassword()));
    }
}
