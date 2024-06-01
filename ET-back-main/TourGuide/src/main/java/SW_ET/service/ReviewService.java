package SW_ET.service;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.Region;
import SW_ET.entity.User;
import SW_ET.repository.ReviewRepository;
import SW_ET.repository.RegionRepository;
import SW_ET.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;


@Service
public class ReviewService {

    private static final Logger log = LoggerFactory.getLogger(ReviewService.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private UserRepository userRepository;
    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }

    // 리뷰 생성
    @Transactional
    public Review createReview(ReviewDto reviewDto) {
        log.info("Creating review with title: {}, text: {}", reviewDto.getReviewTitle(), reviewDto.getReviewText());

        if (reviewDto.getReviewTitle() == null || reviewDto.getReviewText() == null) {
            log.error("Review title or text cannot be null");
            throw new IllegalArgumentException("Review title and text must not be null");
        }
        if (reviewDto.getUserKeyId() == null || reviewDto.getRegionId() == null) {
            log.error("User ID and Region ID cannot be null - User ID: {}, Region ID: {}", reviewDto.getUserKeyId(), reviewDto.getRegionId());
            throw new IllegalArgumentException("User ID and Region ID must not be null");
        }

        User user = userRepository.findById(Long.valueOf(reviewDto.getUserKeyId()))
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID: " + reviewDto.getUserKeyId()));
        Region region = regionRepository.findById(reviewDto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid region ID: " + reviewDto.getRegionId()));

        Review review = new Review();
        review.setUser(user);
        review.setRegion(region);
        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setDatePosted(reviewDto.getDatePosted() != null ? reviewDto.getDatePosted() : LocalDateTime.now());
        review.setUseYn(reviewDto.getUseYn() != null ? reviewDto.getUseYn() : false);
        review.setDeleted(reviewDto.getIsDeleted() != null ? reviewDto.getIsDeleted() : false);

        try {
            review = reviewRepository.save(review);
            log.info("Review saved successfully with ID: {}", review.getReviewId());
            return review;
        } catch (Exception e) {
            log.error("Failed to save review due to: ", e);
            throw new IllegalStateException("Failed to save review due to database integrity issues", e);
        }
    }

    // 리뷰 조회 (JPQL 조인 사용)
    @Transactional
    public ReviewDto getReview(Long reviewId) {
        log.info("Getting review with id: {}", reviewId);
        Review review = reviewRepository.findReviewWithRegion(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 데이터 전달을 위한 DTO 생성과 설정
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewTitle(review.getReviewTitle());
        reviewDto.setReviewText(review.getReviewText());
        reviewDto.setRegionId(review.getRegion().getRegionId());
        log.info("Retrieved review with title: {}", reviewDto.getReviewTitle());
        return reviewDto;
    }

    // 리뷰 수정
    @Transactional
    public Review updateReview(Long reviewId, ReviewDto reviewDto) {
        log.info("Updating review with id: {}", reviewId);
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to update reviews.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        Review updatedReview = reviewRepository.save(review);
        log.info("Updated review with id: {}", updatedReview.getReviewId());
        return updatedReview;
    }

    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        log.info("Deleting review with id: {}", reviewId);
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to delete reviews.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
        log.info("Deleted review with id: {}", reviewId);
    }
}
