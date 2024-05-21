package SW_ET.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ReviewDto {

    private Long reviewId; // Use 'reviewId' to distinguish it from 'userId'
    private Long userId; // ID of the user submitting the review
    private String reviewTitle;
    private String reviewText;
    private LocalDateTime datePosted;
    private LocalDateTime reviewDateModi;
    private Long likeNumber;
    private Long dislikeNumber;
    private Long destinationId;
    private String tags;
    private String useYn;
    private String deletedTrue;
    private LocalDateTime deletedTime;

    // Constructors, getters, and setters


    public ReviewDto() {
    }

    public ReviewDto(Long reviewId, Long userId, String reviewTitle, String reviewText, LocalDateTime datePosted,
                     LocalDateTime reviewDateModi, Long likeNumber, Long dislikeNumber, Long destinationId,
                     String tags, String useYn, String deletedTrue, LocalDateTime deletedTime) {
        this.reviewId = reviewId;
        this.userId = userId;
        this.reviewTitle = reviewTitle;
        this.reviewText = reviewText;
        this.datePosted = datePosted;
        this.reviewDateModi = reviewDateModi;
        this.likeNumber = likeNumber;
        this.dislikeNumber = dislikeNumber;
        this.destinationId = destinationId;
        this.tags = tags;
        this.useYn = useYn;
        this.deletedTrue = deletedTrue;
        this.deletedTime = deletedTime;
    }

    // Getters and setters for all fields
}