package SW_ET.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class CommentDto {
    private Long commentId;
    private Long userId;
    private String username;  // 사용자 이름을 추가하는 것이 좋을 수 있습니다.
    private Long reviewId;
    private String commentText;
    private LocalDateTime commentDate;
    private Long parentCommentId;  // 대댓글의 경우 부모 댓글 ID
    private List<CommentDto> replies;  // 대댓글 목록

    // getters and setters 생략
}
