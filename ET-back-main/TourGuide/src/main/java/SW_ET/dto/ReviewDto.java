package SW_ET.dto;

import lombok.Data;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long reviewId;
    private String userId;
    private Long destinationId;
    private Long festivalId;
    private String reviewTitle;
    private String reviewText;
    private LocalDateTime datePosted;
    private String tags;
    private Boolean isDeleted;
    private Boolean UseYn;
    private List<MultipartFile> imageFiles;
    private String region;  // 지역 정보 추가

    // 지역 설정 메소드 추가
    public void setRegion(String region) {
        this.region = region;
    }
}
