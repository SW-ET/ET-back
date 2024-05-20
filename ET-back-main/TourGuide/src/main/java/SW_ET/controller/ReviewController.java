package SW_ET.controller;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.User;
import SW_ET.service.ReviewService;
import SW_ET.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Review Management", description = "APIs for managing reviews in the system")
public class ReviewController {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private UserService userService;

    @PostMapping
    @Operation(summary = "Create a new review", description = "Create a new review by a logged-in user",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review created successfully"),
                    @ApiResponse(responseCode = "403", description = "Access forbidden", content = @Content),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = @Content)
            })
    public ResponseEntity<?> createReview(@RequestBody ReviewDto reviewDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated() || "anonymousUser".equals(authentication.getPrincipal())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        String username = authentication.getName();
        User user = userService.getUserByUsername(username);
        if (user == null || !user.getUserRole().equals("REVIEWER")) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }

        reviewDto.setUserId(user.getUserKeyId()); // Set the user ID from authenticated user
        Review review = reviewService.createReview(reviewDto);
        if (review == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build(); // Handling null case
        }
        return ResponseEntity.ok(review);
    }

    @GetMapping("/{reviewId}")
    @Operation(summary = "Retrieve a review by its ID", description = "Fetch a review details by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review found", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = Review.class))}),
                    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
            })
    public ResponseEntity<Review> getReviewById(@PathVariable @Parameter(description = "ID of the review to retrieve") Long reviewId) {
        Review review = reviewService.findById(reviewId);
        return review != null ? ResponseEntity.ok(review) : ResponseEntity.notFound().build();
    }

    @PutMapping("/{reviewId}")
    @Operation(summary = "Update a review", description = "Update an existing review by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review updated successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
            })
    public ResponseEntity<Review> updateReview(@PathVariable @Parameter(description = "ID of the review to update") Long reviewId,
                                               @RequestBody ReviewDto reviewDto) {
        reviewDto.setReviewId(reviewId);
        Review updatedReview = reviewService.updateReview(reviewDto);
        return updatedReview != null ? ResponseEntity.ok(updatedReview) : ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{reviewId}")
    @Operation(summary = "Delete a review", description = "Delete a review by its ID",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Review deleted successfully"),
                    @ApiResponse(responseCode = "404", description = "Review not found", content = @Content)
            })
    public ResponseEntity<?> deleteReview(@PathVariable @Parameter(description = "ID of the review to delete") Long reviewId) {
        if (reviewService.deleteReview(reviewId)) {
            return ResponseEntity.ok().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/tag/{tag}")
    @Operation(summary = "Retrieve reviews by tag", description = "Fetch all reviews that contain a specific tag",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Reviews found"),
                    @ApiResponse(responseCode = "404", description = "No reviews found", content = @Content)
            })
    public ResponseEntity<List<Review>> getReviewsByTag(@PathVariable @Parameter(description = "Tag to search for in reviews") String tag) {
        List<Review> reviews = reviewService.getReviewsByTag(tag);
        return reviews.isEmpty() ? ResponseEntity.notFound().build() : ResponseEntity.ok(reviews);
    }
}
