package SW_ET.dto;

import lombok.Data;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.multipart.MultipartFile;
import java.time.LocalDateTime;

@Data
public class ReviewDto {
    private Long reviewId;
    private Long userKeyId;
    private String reviewTitle;
    private String reviewText;

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME) // 날짜형식을 올바르게 LocalDateTIme으로 변환.
    private LocalDateTime datePosted;
    private Boolean isDeleted;
    private Boolean useYn;
    private List<MultipartFile> imageFiles;
    private Long regionId;  // 지역 ID 추가
}
