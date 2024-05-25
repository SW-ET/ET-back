package SW_ET.controller;

import SW_ET.dto.CommentDto;
import SW_ET.dto.ReviewDto;
import SW_ET.entity.Comment;
import SW_ET.entity.Region;
import SW_ET.entity.Review;
import SW_ET.repository.RegionRepository;
import SW_ET.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review Management", description = "Operations pertaining to review management in the application")
@Slf4j
public class ReviewController {

    @Autowired
    private ObjectMapper objectMapper; // JSON 처리를 위한 ObjectMapper

    @Autowired
    private ReviewService reviewService; // ReviewService 주입

    @Operation(summary = "Create or update a review with optional images", description = "Posts a new review or updates an existing one, with the ability to upload images.")
    @ApiResponse(responseCode = "201", description = "Review created or updated successfully", content = @Content(schema = @Schema(implementation = Review.class)))
    @ApiResponse(responseCode = "400", description = "Invalid input data")
    @PostMapping(consumes = {"multipart/form-data"})
    @Autowired
    private RegionRepository regionRepository;

    public ResponseEntity<?> createOrUpdate10 Review(
            @RequestPart("review") String reviewJson,
            @RequestTag(required = false) List<MultipartFile> images) {
        try {
            log.info("Received review data: {}", reviewJson); // Log raw JSON
            ReviewDto reviewDto = objectMapper.readValue(reviewJson, ReviewDto.class);
            log.info("Parsed ReviewDto: {}", reviewDto); // Log parsed DTO

            // "기타" 지역 처리
            if (reviewDto.getDestinationId() == null || reviewDto.getDestinationId().equals(SOME_OTHER_ID)) {
                Region otherRegion = regionRepository.findById(SOME_OTHER_ID)
                        .orElseThrow(() -> new IllegalArgumentException("Region not found"));
                reviewDto.setRegion(otherRegion);  // Set the "기타" region
            }

            Review review = reviewService.createReviewWithImages(reviewDto, images);
            return new ResponseEntity<>(review, HttpStatus.CREATED);
        } catch (IOException e) {
            log.error("Error parsing JSON data", e);
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Error parsing JSON data: " + e.getMessage());
        } catch (Exception e) {
            log.error("Error processing request", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal server error: " + e.getMessage());
        }
    }


    @Operation(summary = "Delete a review", description = "Soft deletes a review by setting a deleted flag.")
    @ApiResponse(responseCode = "200", description = "Review deleted successfully")
    @DeleteMapping("/{reviewId}")
    public ResponseEntity<Void> deleteReview(@PathVariable Long reviewId) {
        reviewService.deleteReview(reviewId);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "Post a comment to a review", description = "Adds a new comment to an existing review.")
    @ApiResponse(responseCode = "200", description = "Comment posted successfully", content = @Content(schema = @Schema(implementation = Comment.class)))
    @PostMapping("/comment")
    public ResponseEntity<Comment> postComment(@RequestBody CommentDto commentDto) {
        Comment comment = reviewService.postComment(commentDto);
        return ResponseEntity.ok(comment);
    }

    @Operation(summary = "Reply to a comment", description = "Posts a reply to an existing comment.")
    @ApiResponse(responseCode = "200", description = "Reply posted successfully", content = @Content(schema = @Schema(implementation = Comment.class)))
    @PostMapping("/reply")
    public ResponseEntity<Comment> replyToComment(@RequestBody CommentDto commentDto) {
        Comment reply = reviewService.replyToComment(commentDto.getParentCommentId(), commentDto.getCommentText(), commentDto.getUserId());
        return ResponseEntity.ok(reply);
    }

    @Operation(summary = "Search reviews by title", description = "Finds reviews that contain the specified title.")
    @ApiResponse(responseCode = "200", description = "Search completed successfully")
    @GetMapping("/search")
    public ResponseEntity<List<Review>> searchReviewsByTitle(@RequestParam String title) {
        List<Review> reviews = reviewService.searchReviewsByTitle(title);
        return ResponseEntity.ok(reviews);
    }
}