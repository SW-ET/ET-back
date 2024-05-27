package SW_ET.crawling;

import SW_ET.dto.RegionDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class RegionDataLoader implements CommandLineRunner {

    private static final Logger logger = LoggerFactory.getLogger(RegionCsvService.class);

    @Autowired
    private RegionCsvService regionCsvService;

    @Autowired
    private RegionService regionService;

    @Override
    public void run(String... args) throws Exception {
        logger.trace("Starting CSV loading process...");
        String csvFilePath = "C:\\Users\\sh980\\workspace\\regions.csv"; // 파일 경로 조정
        List<RegionDto> regionList = regionCsvService.readRegionsFromCsv(csvFilePath);
        logger.trace("CSV data loaded, number of records: " + regionList.size());
        regionService.saveRegions(regionList);
        logger.trace("Regions saved successfully.");
    }
}