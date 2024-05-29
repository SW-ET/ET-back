package SW_ET.service;/*
package SW_ET.service;

import SW_ET.entity.Rating;
import SW_ET.entity.RatingAggregate;
import SW_ET.entity.Review;
import SW_ET.entity.User;
import SW_ET.repository.RatingAggregateRepository;
import SW_ET.repository.RatingRepository;
import SW_ET.repository.ReviewRepository;
import SW_ET.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private RatingAggregateRepository ratingAggregateRepository;

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    // 평점을 리뷰에 추가
    public Rating addRatingToReview(Long reviewId, Double ratingValue, String comment, Long userId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new RuntimeException("Review not found"));

        Rating rating = new Rating();
        rating.setItemId(review.getReviewId());  // 리뷰 ID를 평점 아이템 ID로 설정
        rating.setItemType("REVIEW");  // 아이템 유형을 String으로 설정
        rating.setRating(ratingValue);
        rating.setComment(comment);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        rating.setUser(user);
        rating.setRatingDate(LocalDateTime.now());

        Rating savedRating = ratingRepository.save(rating);

        updateRatingAggregate("REVIEW", review.getReviewId());  // String 값을 사용하여 업데이트

        return savedRating;
    }

    // 다양한 유형의 아이템에 평점 추가
    public Rating addRating(Long itemId, String itemType, Double ratingValue, String comment, Long userId) {
        Rating rating = new Rating();
        rating.setItemId(itemId);
        rating.setItemType(itemType); // 직접 String 값을 할당
        rating.setRating(ratingValue);
        rating.setComment(comment);
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        rating.setUser(user);
        rating.setRatingDate(LocalDateTime.now());

        // 평점 저장
        Rating savedRating = ratingRepository.save(rating);

        // 평점 집계 업데이트
        updateRatingAggregate(itemType, itemId);

        return savedRating;
    }

    private void updateRatingAggregate(String itemType, Long itemId) {
        List<Rating> ratings = ratingRepository.findByItemTypeAndItemId(itemType, itemId);
        double average = ratings.stream()
                .mapToDouble(Rating::getRating)
                .average()
                .orElse(0.0);

        RatingAggregate aggregate = ratingAggregateRepository.findByItemTypeAndItemId(itemType, itemId)
                .orElseGet(() -> new RatingAggregate());

        aggregate.setItemType(itemType);
        aggregate.setItemId(itemId);
        aggregate.setAverageRating(average);

        ratingAggregateRepository.save(aggregate);
    }
}
*/
