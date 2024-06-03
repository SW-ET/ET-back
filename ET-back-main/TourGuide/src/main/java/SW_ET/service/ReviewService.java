package SW_ET.service;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Region;
import SW_ET.entity.Review;
import SW_ET.entity.SubRegion;
import SW_ET.entity.User;
import SW_ET.repository.RegionRepository;
import SW_ET.repository.ReviewRepository;
import SW_ET.repository.SubRegionRepository;
import SW_ET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;


import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ReviewService {

    @Autowired
    private final ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RegionRepository regionRepository;

    @Autowired
    private SubRegionRepository subRegionRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository) {
        this.reviewRepository = reviewRepository;
    }

    public ReviewDto createReview(ReviewDto reviewDto) {
        User user = userRepository.findById(reviewDto.getUserKeyId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
        Region region = regionRepository.findById(reviewDto.getRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid region ID"));
        SubRegion subRegion = subRegionRepository.findById(reviewDto.getSubRegionId())
                .orElseThrow(() -> new IllegalArgumentException("Invalid sub-region ID"));


        Review review = new Review();
        review.setUser(user);
        review.setRegion(region);
        review.setSubRegion(subRegion); // SubRegion 설정
        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setUseYn(reviewDto.getUseYn());
        review.setDatePosted(LocalDateTime.now());

        review = reviewRepository.save(review);

        return convertToDto(review);
    }

    public List<ReviewDto> getAllReviews() {
        return reviewRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public ReviewDto getReviewById(Long id) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));
        return convertToDto(review);
    }

    // 리뷰 수정
    public ReviewDto updateReview(Long id, ReviewDto reviewDto, Long userKeyId) {
        Review existingReview = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 사용자 권한 검증
        if (!existingReview.getUser().getUserKeyId().equals(userKeyId)) {
            throw new AccessDeniedException("You do not have permission to modify this review.");
        }

        updateEntityFromDto(existingReview, reviewDto);
        existingReview = reviewRepository.save(existingReview);
        return convertToDto(existingReview);
    }

    // 리뷰 삭제
    public void deleteReview(Long id, Long userId) {
        Review review = reviewRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        // 사용자 권한 검증
        if (!review.getUser().getUserKeyId().equals(userId)) {
            throw new AccessDeniedException("You do not have permission to delete this review.");
        }

        reviewRepository.deleteById(id);
    }

    private Review convertToEntity(ReviewDto reviewDto) {
        Review review = new Review();
        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setUseYn(reviewDto.getUseYn());
        return review;
    }

    // 지역 ID에 따라 리뷰를 조회하는 메소드
    public List<ReviewDto> getReviewsByRegion(Long regionId) {
        return reviewTransactionalScope(regionId);
    }

    private List<ReviewDto> reviewTransactionalScope(Long regionId){
        return reviewRepository.findByRegionId(regionId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    private ReviewDto convertToDto(Review review) {
        ReviewDto dto = new ReviewDto();

        dto.setReviewId(review.getReviewId());
        if (review.getUser() != null) {
            dto.setUserKeyId(review.getUser().getUserKeyId());  // User의 ID를 가져와서 설정
        }

        if (review.getRegion() != null) {
            dto.setRegionId(review.getRegion().getRegionId()); // Region의 ID를 가져와서 설정
        }

        if (review.getSubRegion() != null) {
            dto.setSubRegionId(review.getSubRegion().getSubRegionId());
        }
        dto.setReviewTitle(review.getReviewTitle());
        dto.setReviewText(review.getReviewText());
        dto.setDatePosted(review.getDatePosted());
        dto.setReviewDateModi(review.getReviewDateModi());
        dto.setDeletedTime(review.getDeletedTime());
        dto.setIsDeleted(review.isDeleted());
        dto.setUseYn(review.isUseYn());

        return dto;
    }

    private void updateEntityFromDto(Review existingReview, ReviewDto reviewDto) {

        existingReview.setReviewTitle(reviewDto.getReviewTitle());
        existingReview.setReviewText(reviewDto.getReviewText());
        existingReview.setUseYn(reviewDto.getUseYn());
        existingReview.setReviewDateModi(LocalDateTime.now());

        if (reviewDto.getDatePosted() != null) {
            existingReview.setDatePosted(reviewDto.getDatePosted());
        }
        if (reviewDto.getReviewDateModi() != null) {
            existingReview.setReviewDateModi(reviewDto.getReviewDateModi());
        }
        if (reviewDto.getDeletedTime() != null) {
            existingReview.setDeletedTime(reviewDto.getDeletedTime());
        }

        if (reviewDto.getIsDeleted() != null) {
            existingReview.setIsDeleted(reviewDto.getIsDeleted());
        }

        // User 및 Region 엔티티 연결 (ID를 통해 조회 후 설정)
        if (reviewDto.getUserKeyId() != null) {
            User user = userRepository.findById(reviewDto.getUserKeyId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));
            existingReview.setUser(user);
        }
        if (reviewDto.getRegionId() != null) {
            Region region = regionRepository.findById(reviewDto.getRegionId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid region ID"));
            existingReview.setRegion(region);
        }

        if (reviewDto.getSubRegionId() != null) {
            SubRegion subRegion = subRegionRepository.findById(reviewDto.getSubRegionId())
                    .orElseThrow(() -> new IllegalArgumentException("Invalid subRegion ID"));
            existingReview.setSubRegion(subRegion);
        }
    }
}
