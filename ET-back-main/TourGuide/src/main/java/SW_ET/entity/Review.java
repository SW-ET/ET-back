package SW_ET.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Reviews")
@Getter
@Setter
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 자동 증가 설정
    @Column(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long id; // 리뷰 ID

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, columnDefinition = "INT UNSIGNED ") // 사용자 ID를 FK로 받음
    private User user; // 유저 정보

    @Column(name = "views", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long views; // 조회수

    @Column(name = "review_title", nullable = false, length = 255)
    private String reviewTitle; // 리뷰 제목

    @Lob
    @Column(name = "review_text", nullable = false)
    private String reviewText; // 리뷰 내용

    @Column(name = "date_posted", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime datePosted; // 작성 일자

    @Column(name = "review_date_modi", nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime reviewDateModi; // 수정 일자

    @Column(name = "likes", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long likeNumber; // 추천

    @Column(name = "dislikes", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long dislikeNumber; // 비추천

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false, columnDefinition = "INT UNSIGNED") // 여행지 ID를 FK로 받음
    private Destination destination; // 여행지 정보

    @Column(name = "use_yn", nullable = false, length = 1)
    // length = 1 .. Y or N으로 문자 알람 가능 여부 표현
    private String useYn; // 알람 가능 여부

    @Column(name = "deleted_time", nullable = true, columnDefinition = "DATETIME")
    private LocalDateTime deletedTime; // 삭제 시간

    @Column(name = "deleted_true", nullable = false, length = 1)
    private String deletedTrue; // 삭제 여부

}
