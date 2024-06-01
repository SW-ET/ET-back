package SW_ET.DataSet;

import SW_ET.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class RegionDataLoader implements CommandLineRunner {

    private final RegionService regionService;


    @Autowired
    public RegionDataLoader(RegionService regionService) {
        this.regionService = regionService;
    }

    @Override
    public void run(String... args) throws Exception {
        regionService.loadRegionsFromJson();
    }
}