package SW_ET.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name="Images") // 테이블 이름을 지정
@Getter
@Setter
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long id;  // 이미지 ID

    @Column(name = "image_url", nullable = false, length = 255)
    private String imageUrl;  // 이미지 주소

    @ManyToOne
    @JoinColumn(name = "review_id", nullable = true)  // 리뷰 ID를 FK로 받음
    private Review review;  // 리뷰 정보

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)  // 유저 ID를 FK로 받음
    private User user;  // 유저 정보

    @Column(name = "created_at", nullable = false, columnDefinition = "DATETIME DEFAULT CURRENT_TIMESTAMP")
    private LocalDateTime createdAt;  // 생성 일자

    @Column(name = "item_type", nullable = false, length = 255)
    private String itemType;  // 이미지가 속한 유형

    @Column(name = "item_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long itemId;  // 이미지가 연결된 항목의 ID

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = true)  // 여행지 ID를 FK로 받음
    private Destination destination;  // 여행지 정보

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = true)  // 축제 ID를 FK로 받음
    private Festival festival;  // 축제 정보
}
