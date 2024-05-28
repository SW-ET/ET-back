package SW_ET.crawling;

import SW_ET.dto.RegionDto;
import SW_ET.entity.Region;
import SW_ET.repository.RegionRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


@Service
public class RegionService {
    private static final Logger logger = LoggerFactory.getLogger(RegionCsvService.class);

    @Autowired
    private RegionRepository regionRepository;

    @Transactional
    public void saveRegions(List<RegionDto> regionDtoList) {
        try {
            for (RegionDto dto : regionDtoList) {
                Region region = new Region();
                region.setRegionName(dto.getRegionName());
                region.setSubRegionName(dto.getSubRegionName());
                regionRepository.save(region);
            }
        } catch (Exception e) {
            logger.error("Error saving regions: " + e.getMessage(), e);
            throw e; // 예외를 다시 던져 상위에서 처리할 수 있도록 합니다.
        }
    }
}
