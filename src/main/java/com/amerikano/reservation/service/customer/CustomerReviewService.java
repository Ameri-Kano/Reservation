package com.amerikano.reservation.service.customer;

import com.amerikano.reservation.entity.Reservation;
import com.amerikano.reservation.entity.Review;
import com.amerikano.reservation.entity.dto.ReviewDto;
import com.amerikano.reservation.entity.dto.customer.PostReviewForm;
import com.amerikano.reservation.entity.dto.customer.UpdateReviewForm;
import com.amerikano.reservation.entity.manager.Shop;
import com.amerikano.reservation.entity.repository.CustomerRepository;
import com.amerikano.reservation.entity.repository.ReservationRepository;
import com.amerikano.reservation.entity.repository.ReviewRepository;
import com.amerikano.reservation.entity.repository.ShopRepository;
import com.amerikano.reservation.exception.ReservationServiceException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.amerikano.reservation.exception.ErrorCode.*;

/**
 * 고객 리뷰 서비스 레이어
 */
@Service
@RequiredArgsConstructor
public class CustomerReviewService {

    private final ReviewRepository reviewRepository;
    private final ReservationRepository reservationRepository;
    private final CustomerRepository customerRepository;
    private final ShopRepository shopRepository;

    /**
     * 서비스 처리 전 해당 고객 유저가 존재하는지 확인
     */
    private boolean isCustomerExists(Long customerId) {
        return customerRepository.existsById(customerId);
    }

    /**
     * 리뷰 정보를 바탕으로 리뷰 등록
     * <p>
     * Shop 레포지토리의 값을 수정하므로 @Transactional 명시
     */
    @Transactional
    public String postReview(Long customerId, PostReviewForm form, String code) {
        // 리뷰가 이미 존재하는지 확인
        Review review = reviewRepository.findByReservationCode(code)
            .orElse(null);

        if (review != null) {
            // 이전에 삭제 처리되었다면 리뷰 수정으로 간주
            if (review.getDeleted()) {
                return updateReview(customerId, PostReviewForm.to(form), code);
            } else {
                throw new ReservationServiceException(REVIEW_ALREADY_EXIST);
            }
        } else {
            // 리뷰가 존재하지 않는 경우 검증 진행 및 리뷰 저장
            Reservation reservation = checkReservation(customerId, code);

            // 해당 예약에 방문하지 않은 경우 리뷰 작성 불가능
            if (!reservation.getVisited()) {
                throw new ReservationServiceException(CANT_POST_REVIEW);
            }

            // 예약했던 매장이 존재하지 않는 경우
            Shop shop = shopRepository.findById(reservation.getShop().getId())
                .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST));

            Review newReview = getNewReview(customerId, shop.getId(), form, code);

            reviewRepository.save(newReview);
            shop.getReviews().add(newReview);

            Double newRate = calculateRate(shop);

            shop.setRate(newRate);

            return code + " 예약에 대한 리뷰 등록이 완료되었습니다.";
        }
    }

    /**
     * 등록한 리뷰 수정
     */
    @Transactional
    public String updateReview(Long customerId, UpdateReviewForm form, String code) {
        Reservation reservation = checkReservation(customerId, code);

        Shop shop = shopRepository.findById(reservation.getShop().getId())
            .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST));

        Review review = checkReview(code);

        review.setTitle(form.getTitle());
        review.setDetail(form.getDetail());
        review.setRate(form.getRate().getRateValue());

        if (review.getDeleted()) {
            review.setDeleted(false);
        }

        Double newRate = calculateRate(shop);

        shop.setRate(newRate);

        return code + " 예약에 대한 리뷰 변경이 완료되었습니다.";
    }

    /**
     * 등록한 리뷰 삭제
     */
    @Transactional
    public String deleteReview(Long customerId, String code) {
        Reservation reservation = checkReservation(customerId, code);

        Shop shop = shopRepository.findById(reservation.getShop().getId())
            .orElseThrow(() -> new ReservationServiceException(SHOP_NOT_EXIST));

        Review review = checkReview(code);

        review.setDeleted(true);

        Double newRate = calculateRate(shop);

        shop.setRate(newRate);

        return code + " 예약에 대한 리뷰 삭제가 완료되었습니다.";
    }

    /**
     * 해당 고객이 작성한 리뷰 조회
     */
    public List<ReviewDto> searchReviews(Long customerId, Integer page) {
        // 페이지 파라미터가 1보다 작은 경우
        if (page < 1) {
            throw new ReservationServiceException(WRONG_PAGE_INDEX);
        }

        return reviewRepository.findAllByCustomerIdAndDeletedIsFalseOrderByLastModifiedAtDesc(
            customerId, PageRequest.of(page - 1, 10))
            .stream()
            .map(ReviewDto::from).collect(Collectors.toList());
    }


    /**
     * 리뷰 엔티티 생성
     */
    private Review getNewReview(Long customerId, Long shopId, PostReviewForm form, String code) {
        return Review.builder()
            .customerId(customerId)
            .shopId(shopId)
            .reservationCode(code)
            .title(form.getTitle())
            .detail(form.getDetail())
            .rate(form.getRate().getRateValue())
            .deleted(false)
            .build();
    }

    /**
     * 매장의 전체평점 다시 계산
     */
    private Double calculateRate(Shop shop) {
        return shop.getReviews()
            .stream()
            .filter(review -> !review.getDeleted())
            .mapToDouble(Review::getRate)
            .average()
            .orElse(0.0);
    }

    /**
     * 예약 정보 검증
     */
    private Reservation checkReservation(Long customerId, String code) {
        // 해당 유저가 존재하지 않는 경우 처리
        if (!isCustomerExists(customerId)) {
            throw new ReservationServiceException(RESERVATION_CUSTOMER_NOT_EXIST);
        }

        // 해당 예약이 존재하지 않다면 예외, 존재한다면 반환
        return reservationRepository.findByCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));
    }

    /**
     * 해당 리뷰가 존재하는지 확인
     */
    private Review checkReview(String code) {
        return reviewRepository.findByReservationCode(code)
            .orElseThrow(() -> new ReservationServiceException(RESERVATION_NOT_EXIST));
    }
}
