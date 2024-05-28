package SW_ET.service;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.Region;
import SW_ET.repository.ReviewRepository;
import SW_ET.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private RegionRepository regionRepository;

    private boolean isAuthenticated() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication != null && authentication.isAuthenticated() && !"anonymousUser".equals(authentication.getPrincipal());
    }  // 권한 검증

    // 리뷰 생성
    @Transactional
    public Review createReview(ReviewDto reviewDto) {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to create reviews.");
        }

        Region region = regionRepository.findById(reviewDto.getRegionId())
                .orElseThrow(() -> new RuntimeException("Region not found"));

        Review review = new Review();
        review.setRegion(region);
        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setDatePosted(reviewDto.getDatePosted());
        review.setUseYn(true);

        return reviewRepository.save(review);
    }


    // 리뷰 조회 (JPQL 조인 사용)
    @Transactional
    public ReviewDto getReview(Long reviewId) {
        Review review = reviewRepository.findReviewWithRegion(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 데이터 전달을 위한 DTO 생성과 설정
        ReviewDto reviewDto = new ReviewDto();
        reviewDto.setReviewTitle(review.getReviewTitle());
        reviewDto.setReviewText(review.getReviewText());
        reviewDto.setRegionName(review.getRegion().getRegionName());
        reviewDto.setSubRegionName(review.getRegion().getSubRegionName());
        return reviewDto;
    }

    // 리뷰 수정
    @Transactional
    public Review updateReview(Long reviewId, ReviewDto reviewDto) {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to update reviews.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        return reviewRepository.save(review);
    }


    // 리뷰 삭제
    @Transactional
    public void deleteReview(Long reviewId) {
        if (!isAuthenticated()) {
            throw new SecurityException("Authentication required to delete reviews.");
        }

        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        reviewRepository.delete(review);
    }
}