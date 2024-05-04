package SW_ET.entity;

import jakarta.persistence.*;

@Entity
@Table
public class RatingAggregate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)  // 자동 증가 설정
    @Column(name = "ra_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long id;  // 집계 테이블의 ID

    @Column(name = "average_rating", nullable = false, columnDefinition = "DECIMAL(2, 1)")
    private Double averageRating;  // 평점 평균

    @ManyToOne
    @JoinColumn(name = "destination_id", nullable = true)  // 여행지 ID를 FK로 받음
    private Destination destination;  // 여행지 정보

    @ManyToOne
    @JoinColumn(name = "festival_id", nullable = true)  // 축제 ID를 FK로 받음
    private Festival festival;  // 축제 정보
}
