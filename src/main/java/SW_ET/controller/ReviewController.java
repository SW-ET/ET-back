package SW_ET.controller;

import SW_ET.config.JwtTokenProvider;
import SW_ET.dto.ReviewDto;
import SW_ET.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@Controller
@RequestMapping("/api/reviews")
@Slf4j
public class ReviewController {


    @Autowired
    private ReviewService reviewService;

    @Autowired
    private ObjectMapper objectMapper; // ObjectMapper를 빈으로 주입받음

    private static final Logger logger = LoggerFactory.getLogger(ReviewController.class);

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    // 홈 페이지로 이동
    @GetMapping("/home")
    public String showHomePage() {
        return "reviews/home";  // "home"은 home.html 또는 홈 페이지의 뷰 이름
    }

    // 리뷰 등록 폼 페이지로 이동
    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        model.addAttribute("review", new ReviewDto());  // 리뷰 등록을 위한 빈 DTO
        return "reviews/register";  // "register"는 register.html 또는 등록 폼의 뷰 이름
    }

    // 리뷰 생성
    @PostMapping("/register")
    public ResponseEntity<ReviewDto> createReview(@RequestPart("review") String reviewDtoJson,
                                                  @RequestParam(value = "image", required = false) MultipartFile imageFile) {
        try {
            log.info("Received review data: {}", reviewDtoJson);

            ReviewDto reviewDto = objectMapper.readValue(reviewDtoJson, ReviewDto.class);

            if (imageFile != null && !imageFile.isEmpty()) {
                log.info("Received image file: {}", imageFile.getOriginalFilename());
                String imageUrl = reviewService.saveImage(imageFile);
                log.info("Image URL: {}", imageUrl);
                reviewDto.setImageUrl(imageUrl);  // 이미지 URL을 DTO에 설정
            }

            ReviewDto savedReview = reviewService.createReview(reviewDto);
            log.info("Review saved successfully: {}", savedReview);
            return ResponseEntity.ok(savedReview);

        } catch (Exception e) {
            log.error("Exception occurred while creating the review", e);
            return ResponseEntity.badRequest().body(null);
        }
    }

    // 시/도에 대한 리뷰 조회
    @GetMapping("/region/{regionId}")
    public ResponseEntity<?> getReviewsByRegion(@PathVariable Long regionId) {
        List<ReviewDto> reviews = reviewService.getReviewsByRegion(regionId);
        if (reviews.isEmpty()) { // regionId(시/도)에 대한 리뷰가없으면 검색된 리뷰가 없습니다. 출력
            return ResponseEntity.ok("검색된 리뷰가 없습니다.");
        }
        return ResponseEntity.ok(reviews);
    }

    // 특정 리뷰 상세 조회
    @GetMapping("/{id}")
        public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
            ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    // 리뷰 업데이트
    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id,
                                                  @RequestPart("review") ReviewDto reviewDto,  // Directly use ReviewDto
                                                  @RequestPart(value = "image", required = false) MultipartFile imageFile,
                                                  HttpServletRequest request) {
        logger.debug("Attempting to update review with ID: {}", id);
        String token = jwtTokenProvider.resolveToken(request);
        Long userKeyId = jwtTokenProvider.getUserKeyIdFromToken(token);
        logger.debug("User ID extracted from token: {}", userKeyId);

        try {
            if (imageFile != null && !imageFile.isEmpty()) {
                String imageUrl = reviewService.saveImage(imageFile);  // 이미지 저장
                reviewDto.setImageUrl(imageUrl);  // 이미지 URL을 DTO에 설정
            }
            ReviewDto updatedReview = reviewService.updateReview(id, reviewDto, imageFile, userKeyId);
            return ResponseEntity.ok(updatedReview);
        } catch (Exception e) {
            logger.error("Error updating review", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // 리뷰 삭제
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMyReview(@PathVariable Long id, HttpServletRequest request) {
        logger.debug("Attempting to delete review with ID: {}", id);
        String token = jwtTokenProvider.resolveToken(request);
        Long userKeyId = jwtTokenProvider.getUserKeyIdFromToken(token);
        logger.debug("User userKeyID extracted from token: {}", userKeyId);
        reviewService.deleteReview(id, userKeyId);
        logger.debug("Review deleted successfully.");
        return ResponseEntity.ok().build();
    }
}
