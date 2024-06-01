package SW_ET.controller;

import SW_ET.dto.RegionDto;
import SW_ET.dto.RegionGroupDto;
import SW_ET.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/regions")
public class RegionController {

    private final RegionService regionService;

    @Autowired
    public RegionController(RegionService regionService) {
        this.regionService = regionService;
    }

    // 지역 그룹과 각 그룹에 포함된 지역 및 하위 지역 정보를 가져오는 API
    @GetMapping("/groups") // 헤더에서 사용
    public ResponseEntity<List<RegionGroupDto>> getAllRegionGroups() {
        List<RegionGroupDto> groups = regionService.getAllRegionGroups();
        return ResponseEntity.ok(groups);
    }

    // 모든 지역과 각 지역의 하위 지역 정보를 가져오는 API
    @GetMapping  // 사용자가 리뷰 적을때 지역을 선택할 때 사용
    public ResponseEntity<List<RegionDto>> getAllRegions() {
        List<RegionDto> regions = regionService.getAllRegions();
        return ResponseEntity.ok(regions);
    }
}