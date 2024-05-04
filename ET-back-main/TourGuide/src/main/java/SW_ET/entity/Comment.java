package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comments")
@Getter
@Setter
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 설정
    @Column(name = "comment_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long id;  // 댓글 ID

    @ManyToOne
    @JoinColumn(name = "parent_comment_id", nullable = true)  // 상위 댓글 ID를 FK로 받음
    private Comment parentComment;  // 상위 댓글

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 사용자 ID를 FK로 받음
    private User user;  // 유저 정보

    @ManyToOne // comment : 다 , review : 일
    @JoinColumn(name = "review_id", nullable = false)  // 리뷰 ID를 FK로 받음
    private Review review;  // 리뷰 정보

    @Lob
    @Column(name = "content", nullable = false)
    private String content;  // 댓글 내용

    @Column(name = "depth", nullable = false, columnDefinition = "INT UNSIGNED")
    private Integer depth;  // 댓글 깊이

    @Column(name = "post_time", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime postTime;  // 작성 시간

    @Column(name = "modify_date_time", nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime modifyDateTime;  // 수정 시간
}
