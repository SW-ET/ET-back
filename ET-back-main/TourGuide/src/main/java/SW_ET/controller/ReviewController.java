package SW_ET.controller;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.ReviewImages;
import SW_ET.service.ReviewImageService;
import SW_ET.service.ReviewService;
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

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ReviewImageService reviewImageService;

    // 리뷰 생성
    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<?> createReview(@ModelAttribute ReviewDto reviewDto, @RequestParam("files") List<MultipartFile> files) {
        try {
            Review createdReview = reviewService.createReview(reviewDto);
            List<ReviewImages> images = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    ReviewImages savedImage = reviewImageService.storeImage(file, createdReview.getReviewId());
                    savedImage.setReview(createdReview);  // 이미지와 리뷰 연결
                    images.add(savedImage);
                }
            }
            createdReview.setReviewImages(images);  // 리뷰에 이미지 리스트 설정
            return ResponseEntity.ok(createdReview);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("리뷰 생성 실패: " + e.getMessage());
        }
    }

    // 리뷰 조회
    @GetMapping("/{id}")
    public ResponseEntity<ReviewDto> getReview(@PathVariable Long id) {
        try {
            ReviewDto reviewDto = reviewService.getReview(id);
            return ResponseEntity.ok(reviewDto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // 리뷰 수정
    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<?> updateReview(@PathVariable Long id, @ModelAttribute ReviewDto reviewDto, @RequestParam("files") List<MultipartFile> files) {
        try {
            Review updatedReview = reviewService.updateReview(id, reviewDto);
            List<ReviewImages> updatedImages = new ArrayList<>();
            for (MultipartFile file : files) {
                if (!file.isEmpty()) {
                    ReviewImages updatedImage = reviewImageService.storeImage(file, updatedReview.getReviewId());
                    updatedImage.setReview(updatedReview);
                    updatedImages.add(updatedImage);
                }
            }
            updatedReview.setReviewImages(updatedImages);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("리뷰 수정 실패: " + e.getMessage());
        }
    }

    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long id) {
        try {
            reviewService.deleteReview(id);
            return ResponseEntity.ok().build();
        } catch (SecurityException e) {
            return ResponseEntity.status(403).build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}