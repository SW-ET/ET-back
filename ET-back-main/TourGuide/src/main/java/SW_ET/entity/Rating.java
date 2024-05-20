package SW_ET.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table
public class Rating {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 설정
    @Column(name = "rating_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long ratingId; // 평점 고유 식별자

    @Column(name = "item_type", nullable = false, length = 255)
    private String itemType;  // 항목 평점 유형

    @Column(name = "item_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long itemId;  // 항목 유형에 따른 ID

    @Column(name = "rating", nullable = false, columnDefinition = "DECIMAL(3, 2)")
    private Double rating;  // 평점

    @Column(name = "comment", nullable = true, columnDefinition = "TEXT")
    private String ratingComment;  // 코멘트

    @Column(name = "comment_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime ratingCommentAt;  // 생성 시간

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 여행지 ID를 FK로 받음
    private Destination destination;  // 여행지 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 축제 ID를 FK로 받음
    private Festival festival;  // 축제 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 유저 ID를 FK로 받음
    private User user;  // 유저 정보
}
