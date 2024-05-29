package SW_ET.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;

@Entity
@Getter
@Setter
@Table(name = "Region")
public class Region {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "region_id", nullable = false, columnDefinition = "INT UNSIGNED")
    private Long regionId;

    @Column(name = "region_name", nullable = false, length = 255)
    private String regionName;  // 서울시, 경기도 등등

    @ManyToOne(fetch = FetchType.LAZY) // 상위 지역 참조
    @JoinColumn(name = "parent_id") // 외래키로 parent_id 사용
    private Region parent;

    @Column(name = "subRegion_name", nullable = false, length = 255)
    private String subRegionName; // 강남구, 은평구 등등

    @OneToMany(mappedBy = "parent", cascade = CascadeType.ALL, orphanRemoval = true) // 하위 지역 목록
    private Set<Region> children;

    @OneToMany(mappedBy = "region", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Destination> destinations;
}