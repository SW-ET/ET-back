package SW_ET.crawling;

import SW_ET.dto.RegionDto;
import com.opencsv.CSVReader;
import com.opencsv.CSVReaderBuilder;

import java.io.Reader;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
public class RegionCsvService {
    private static final Logger logger = LoggerFactory.getLogger(RegionCsvService.class);

    public List<RegionDto> readRegionsFromCsv(String filePath) throws Exception {
        try {
            Reader reader = Files.newBufferedReader(Paths.get(filePath), StandardCharsets.UTF_8);
            CSVReader csvReader = new CSVReaderBuilder(reader).withSkipLines(1).build();
            return csvReader.readAll().stream().map(data -> {
                RegionDto dto = new RegionDto();
                dto.setRegionName(data[0]);
                dto.setSubRegionName(data[1]);
                return dto;
            }).collect(Collectors.toList());
        } catch (IOException e) {
            logger.error("Error reading CSV file at path: " + filePath, e);
            throw new RuntimeException("Error reading CSV file", e);
        }
    }
}