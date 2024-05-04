package SW_ET.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "FestivalRegion")
@Getter
@Setter
public class FestivalRegion {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 지동 증가
    @Column(name = "fr_id", nullable = false, columnDefinition = "INT UNSIGNED AUTO_INCREMENT")
    private Long id; // 중간 테이블의 ID

    @ManyToOne
    @JoinColumn(name = "fesival_id", nullable = false) // 축제 ID를 FK로 받음
    private Festival festival;

    @ManyToOne
    @JoinColumn(name = "region_id", nullable = false) // 지역 ID를 FK로
    private Region region;

}
