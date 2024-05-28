package SW_ET.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor  // 모든 필드를 포함하는 생성자를 자동으로 생성
@NoArgsConstructor   // 기본 생성자도 생성 (JPA나 다른 라이브러리에 필요할 수 있음)
public class ReviewImageDto {
    private Long imageId;
    private String imageUrl;
    private String imagePath;
    private String fileName;
    private String contentType;
    private Long fileSize;
    private Long reviewId;
    private Long userId;
}
