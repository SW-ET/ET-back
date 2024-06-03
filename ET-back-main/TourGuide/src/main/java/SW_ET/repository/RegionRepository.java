package SW_ET.repository;

import SW_ET.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT r FROM Region r WHERE r.regionGroup.RegionGroupId = :groupId")
    List<Region> findByRegionGroupId(@Param("groupId") Long groupId);
}