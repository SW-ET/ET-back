package SW_ET.service;

import SW_ET.entity.Region;
import SW_ET.repository.RegionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RegionService {

    @Autowired
    private RegionRepository regionRepository;

    public List<Region> findTopRegions() {
        // RegionRepository에서 정의된 JPQL 쿼리를 호출
        return regionRepository.findTopRegions();
    }

    public List<Region> findSubRegionsByParentId(Long parentId) {
        // RegionRepository에서 정의된 JPQL 쿼리를 호출
        return regionRepository.findSubRegionsByParentId(parentId);
    }
}
