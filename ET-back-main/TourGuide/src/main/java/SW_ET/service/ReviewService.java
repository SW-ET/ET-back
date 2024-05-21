package SW_ET.service;

import SW_ET.dto.ReviewDto;
import SW_ET.entity.Review;
import SW_ET.entity.User;
import SW_ET.entity.Destination;
import SW_ET.repository.ReviewRepository;
import SW_ET.repository.UserRepository;
import SW_ET.repository.DestinationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DestinationRepository destinationRepository;

    public Review createReview(ReviewDto reviewDto) {
        Optional<User> user = userRepository.findById(reviewDto.getUserId());
        Optional<Destination> destination = destinationRepository.findById(reviewDto.getDestinationId());

        Review review = new Review();
        user.ifPresent(review::setUser);
        destination.ifPresent(review::setDestination);

        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setTags(reviewDto.getTags());
        review.setDatePosted(LocalDateTime.now()); // Defaulting to now if not provided
        review.setReviewDateModi(reviewDto.getReviewDateModi());
        review.setLikeNumber(reviewDto.getLikeNumber());
        review.setDislikeNumber(reviewDto.getDislikeNumber());
        review.setUseYn(reviewDto.getUseYn());
        review.setDeletedTrue(reviewDto.getDeletedTrue());
        review.setDeletedTime(reviewDto.getDeletedTime());

        return reviewRepository.save(review);
    }

    public List<Review> getReviewsByTag(String tag) {
        return reviewRepository.findByTagsContaining(tag);
    }

    public Review findById(Long reviewId) {
        return reviewRepository.findById(reviewId).orElse(null);
    }

    public Review updateReview(ReviewDto reviewDto) {
        Optional<Review> reviewOptional = reviewRepository.findById(reviewDto.getReviewId());
        if (!reviewOptional.isPresent()) {
            return null; // Or throw an exception indicating not found
        }

        Review review = reviewOptional.get();
        review.setReviewTitle(reviewDto.getReviewTitle());
        review.setReviewText(reviewDto.getReviewText());
        review.setTags(reviewDto.getTags());
        review.setReviewDateModi(LocalDateTime.now()); // Updating the modification date
        review.setLikeNumber(reviewDto.getLikeNumber());
        review.setDislikeNumber(reviewDto.getDislikeNumber());
        return reviewRepository.save(review);
    }

    public boolean deleteReview(Long reviewId) {
        if (reviewRepository.existsById(reviewId)) {
            reviewRepository.deleteById(reviewId);
            return true;
        }
        return false;
    }
}