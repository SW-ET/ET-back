package SW_ET.controller;

import SW_ET.dto.RegionDto;
import SW_ET.dto.RegionGroupDto;
import SW_ET.dto.SubRegionDto;
import SW_ET.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
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

    // 모든 지역 그룹 정보를 가져오는 API
    @GetMapping("/groups")
    public ResponseEntity<List<RegionGroupDto>> getAllRegionGroups() {
        List<RegionGroupDto> groups = regionService.getAllRegionGroups();
        return ResponseEntity.ok(groups);
    }

    // 지역 그룹에 속하는 메인 지역만 반환 (ex 수도권 -> 경기도  .. 헤더에서 쓰일 메소드)
    @GetMapping("/{groupId}/main-regions")
    public ResponseEntity<List<RegionDto>> getRegionsByGroup(@PathVariable Long groupId) {
        List<RegionDto> regions = regionService.getRegionsByGroup(groupId);
        return ResponseEntity.ok(regions);
    }


    // 메인 지역 ID에 따라 하위 지역 반환
    @GetMapping("/{RegionId}/sub-regions")
    public ResponseEntity<List<SubRegionDto>> getSubRegionsByRegion(@PathVariable Long RegionId) {
        List<SubRegionDto> subRegions = regionService.getSubRegionsByRegion(RegionId);
        return ResponseEntity.ok(subRegions);
    }
}