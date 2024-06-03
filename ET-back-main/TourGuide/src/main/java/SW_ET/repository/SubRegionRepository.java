package SW_ET.repository;

import SW_ET.entity.SubRegion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubRegionRepository extends JpaRepository<SubRegion, Long> {
    @Query("SELECT sr FROM SubRegion sr WHERE sr.region.regionId = :regionId")
    List<SubRegion> findByRegionId(@Param("regionId") Long regionId);
}