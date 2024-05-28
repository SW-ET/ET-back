package SW_ET.dto;

import lombok.Data;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long reviewId;
    private String userKeyId;
    private String reviewTitle;
    private String reviewText;
    private LocalDateTime datePosted;
    private Boolean isDeleted;
    private Boolean UseYn;
    private String regionName;
    private String subRegionName;
    private List<MultipartFile> imageFiles;
    private Long regionId;  // 지역 ID 추가
}
