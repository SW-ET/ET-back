package SW_ET.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewDto {
    private Long reviewId;
    private Long userKeyId;
    private String reviewTitle;
    private String reviewText;
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime datePosted;
    private LocalDateTime reviewDateModi;
    private Boolean isDeleted;
    private LocalDateTime deletedTime;
    private Boolean useYn;
    private Long regionId;
    private Long subRegionId;
    private String imageUrl;
}
