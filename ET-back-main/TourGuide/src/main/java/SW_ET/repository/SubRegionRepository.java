package SW_ET.repository;

import SW_ET.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRegionRepository extends JpaRepository<SubRegion, Long> {
    List<SubRegion> findByRegionRegionId(Long regionId);
}