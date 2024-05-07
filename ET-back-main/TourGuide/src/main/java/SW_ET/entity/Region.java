package SW_ET.entity;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Regions")
@Getter
@Setter
public class Region {

    @Id
    @GeneratedValue
    @Column(name = "region_id", nullable = false, columnDefinition  = "INT UNSIGNED AUTO_INCREMENT")
    private Long regionId;

    @Column(name = "region", nullable = false, length = 255)
    private String region;

    @Column(name = "sub_region", nullable = false, length = 255)
    private String subRegion;

}
