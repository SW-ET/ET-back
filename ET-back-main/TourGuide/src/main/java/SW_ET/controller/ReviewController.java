package SW_ET.controller;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.ReviewImages;
import SW_ET.service.ReviewImageService;
import SW_ET.service.ReviewService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/reviews")  // 리뷰 관련 요청의 기본 URL
public class ReviewController {

    private static final Logger log = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewImageService reviewImageService;

    // 리뷰 생성
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createReview(
            @RequestPart("reviewDto") ReviewDto reviewDto,
            @RequestPart("files") List<MultipartFile> files) {
        log.info("Received request to create review with title: {}, text: {}", reviewDto.getReviewTitle(), reviewDto.getReviewText());
        try {
            // 리뷰 생성
            Review createdReview = reviewService.createReview(reviewDto);
            if (createdReview.getReviewId() == null) {
                log.error("Review creation failed: Review ID is null after save.");
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to create review: Review ID is not generated.");
            }

            // 이미지 저장
            List<ReviewImages> images = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                images = reviewImageService.storeImages(files, createdReview.getReviewId());
            }

            // 이미지 리스트를 리뷰 객체에 설정
            createdReview.setReviewImages(images);
            log.info("Review created successfully with ID: {}", createdReview.getReviewId());

            // 성공 응답 반환
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            log.error("Error creating review: {}", e.getMessage(), e);
            return ResponseEntity.badRequest().body("Review creation failed: " + e.getMessage());
        }
    }
    // 리뷰 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
        log.info("Received request to get review with id: {}", id);
        try {
            ReviewDto reviewDto = reviewService.getReview(id);
            return ResponseEntity.ok(reviewDto);
        } catch (RuntimeException e) {
            log.error("Review not found: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }

    // 리뷰 수정
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateReview(@PathVariable Long id, @RequestPart("reviewDto") @Valid ReviewDto reviewDto, @RequestParam(value = "files", required = false) List<MultipartFile> files) {
        log.info("Received request to update review with id: {}", id);
        try {
            Review updatedReview = reviewService.updateReview(id, reviewDto);
            List<ReviewImages> updatedImages = new ArrayList<>();
            if (files != null && !files.isEmpty()) {
                for (MultipartFile file : files) {
                    if (!file.isEmpty()) {
                        ReviewImages updatedImage = reviewImageService.storeImage(file, updatedReview.getReviewId());
                        updatedImage.setReview(updatedReview);
                        updatedImages.add(updatedImage);
                    }
                }
                updatedReview.setReviewImages(updatedImages); // 올바른 메서드 이름으로 수정
            }
            log.info("Review updated successfully.");
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            log.error("Error updating review: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 수정 실패: " + e.getMessage());
        }
    }



    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        log.info("Received request to delete review with id: {}", id);
        try {
            reviewService.deleteReview(id);
            log.info("Review deleted successfully.");
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            log.error("SecurityException when deleting review: {}", e.getMessage(), e);
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            log.error("RuntimeException when deleting review: {}", e.getMessage(), e);
            return ResponseEntity.notFound().build();
        }
    }
}
