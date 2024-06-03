package SW_ET.controller;

import SW_ET.config.JwtTokenProvider;
import SW_ET.dto.ReviewDto;
import SW_ET.service.ReviewService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/api/reviews")
public class ReviewController {


    private final ReviewService reviewService;

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
    @PostMapping
    public ResponseEntity<ReviewDto> createReview(@RequestBody ReviewDto reviewDto) {
        ReviewDto savedReview = reviewService.createReview(reviewDto);
        return ResponseEntity.ok(savedReview);
    }

    // 모든 리뷰 조회
    @GetMapping
    public ResponseEntity<List<ReviewDto>> getAllReviews() {
        List<ReviewDto> reviews = reviewService.getAllReviews();
        return ResponseEntity.ok(reviews);
    }

    // 특정 리뷰 상세 조회
    @GetMapping("/{id}")
        public ResponseEntity<ReviewDto> getReviewById(@PathVariable Long id) {
            ReviewDto review = reviewService.getReviewById(id);
        return ResponseEntity.ok(review);
    }

    // 리뷰 업데이트
    @PutMapping("/{id}")
    public ResponseEntity<ReviewDto> updateReview(@PathVariable Long id, @RequestBody ReviewDto reviewDto, HttpServletRequest request) {
        logger.debug("Attempting to update review with ID: {}", id);
        String token = jwtTokenProvider.resolveToken(request);
        Long userKeyId = jwtTokenProvider.getUserKeyIdFromToken(token);
        logger.debug("User ID extracted from token: {}", userKeyId);
        ReviewDto updatedReview = reviewService.updateReview(id, reviewDto, userKeyId);
        logger.debug("User ID extracted from token: {}", userKeyId);
        return ResponseEntity.ok(updatedReview);
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
