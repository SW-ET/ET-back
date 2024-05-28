package SW_ET.controller;

import SW_ET.dto.RegionDto;
import SW_ET.entity.Region;
import SW_ET.service.RegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/regions")
public class RegionController {

    @Autowired
    private RegionService regionService;

    // 모든 상위 지역 목록을 조회
    @GetMapping("/top")
    public ResponseEntity<List<RegionDto>> getAllTopRegions() {
        List<Region> regions = regionService.findTopRegions();
        List<RegionDto> dtos = regions.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    // 특정 상위 지역의 하위 지역 목록을 조회
    @GetMapping("/{regionId}/subregions")
    public ResponseEntity<List<RegionDto>> getSubRegions(@PathVariable Long regionId) {
        List<Region> subRegions = regionService.findSubRegionsByParentId(regionId);
        List<RegionDto> dtos = subRegions.stream().map(this::convertToDto).collect(Collectors.toList());
        return ResponseEntity.ok(dtos);
    }

    private RegionDto convertToDto(Region region) {
        RegionDto dto = new RegionDto();
        dto.setRegionId(region.getRegionId());
        dto.setRegionName(region.getRegionName());
        dto.setSubRegionName(region.getSubRegionName());
        return dto;
    }
}
