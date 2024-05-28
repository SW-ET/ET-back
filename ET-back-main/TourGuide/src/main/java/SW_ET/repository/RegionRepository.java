package SW_ET.repository;

import SW_ET.entity.Region;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RegionRepository extends JpaRepository<Region, Long> {

    @Query("SELECT r FROM Region r WHERE r.parent IS NULL")
    List<Region> findTopRegions(); // 상위 지역만 조회

    @Query("SELECT r FROM Region r WHERE r.parent.id = :parentId")
    List<Region> findSubRegionsByParentId(@Param("parentId") Long parentId); // 하위 지역만 조회
}
