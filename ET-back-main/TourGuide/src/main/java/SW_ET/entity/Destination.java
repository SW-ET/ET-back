package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Destinations")  // 테이블 이름을 지정
@Getter
@Setter
public class Destination {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "destination_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long destinationId; // 여행지 ID (기본 키)

    @Column(name = "destination_name", nullable = false, length = 255)
    private String destinationName; // 여행지 이름

    @Column(name = "description", length = 255)
    // 크롤링 해 올 여행지 간단 설명
    private String destinationDescription; // 여행지 설명

    @Column(name = "location", nullable = false, length = 255)
    private String destinationLocation;  // 여행지 주소
}
