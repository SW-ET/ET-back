package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "Favorites")
@Getter
@Setter
public class Favorite {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "favorite_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long id; // 찜목록 ID

    @Column(name = "item_type", nullable = false)
    private String itemType;  // 찜하는 항목의 유형

    @Column(name = "created_at" , nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;   // 생성 일자

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_key_id", nullable = false, columnDefinition = "INT UNSIGNED") // 유저 ID를 FK로 받음
    private User user; // 유저 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "destination_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 여행지 ID를 FK로 받음
    private Destination destination;  // 여행지 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "festival_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 축제 ID를 FK로 받음
    private Festival festival;  // 축제 정보

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "review_id", nullable = false, columnDefinition = "INT UNSIGNED")  // 리뷰 ID를 FK로 받음
    private Review review;  // 리뷰 정보


}
