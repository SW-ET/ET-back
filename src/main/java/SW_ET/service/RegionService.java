package SW_ET.service;

import SW_ET.dto.RegionDto;
import SW_ET.dto.RegionGroupDto;
import SW_ET.dto.SubRegionDto;
import SW_ET.entity.*;
import SW_ET.repository.RegionGroupRepository;
import SW_ET.repository.RegionRepository;
import SW_ET.repository.SubRegionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class RegionService {

    private final ObjectMapper objectMapper;
    private final RegionRepository regionRepository;
    private final SubRegionRepository subRegionRepository;
    private final RegionGroupRepository regionGroupRepository;

    @Autowired
    public RegionService(ObjectMapper objectMapper, RegionRepository regionRepository, SubRegionRepository subRegionRepository, RegionGroupRepository regionGroupRepository) {
        this.objectMapper = objectMapper;
        this.regionRepository = regionRepository;
        this.subRegionRepository = subRegionRepository;
        this.regionGroupRepository = regionGroupRepository;
    }

    // regions.json을 db에 넣는 메소드
    @Transactional
    public void loadRegionsFromJson() throws IOException {
        InputStream is = new FileInputStream("C:\\Users\\sh980\\workspace\\regions.json");
        ObjectMapper objectMapper = new ObjectMapper();
        List<RegionGroupData> regionGroups = objectMapper.readValue(is, new TypeReference<List<RegionGroupData>>() {});

        for (RegionGroupData groupData : regionGroups) {
            RegionGroup regionGroup = new RegionGroup();
            regionGroup.setRegionGroupName(groupData.getName());
            regionGroup = regionGroupRepository.save(regionGroup);

            for (RegionData data : groupData.getRegions()) {
                Region region = new Region();
                region.setRegionName(data.getRegionName());
                region.setRegionGroup(regionGroup);
                region = regionRepository.save(region);

                for (String subRegionName : data.getSubRegions()) {
                    SubRegion subRegion = new SubRegion();
                    subRegion.setSubRegionName(subRegionName);
                    subRegion.setRegion(region);
                    subRegionRepository.save(subRegion);
                }
            }
        }
    }


    // 지역 그룹에 속하는 메인 지역만 반환 (ex 수도권 -> 경기도  .. 헤더에서 쓰일 메소드)
    public List<RegionDto> getRegionsByGroup(Long groupId) {
        List<Region> regions = regionRepository.findByRegionGroupId(groupId);
        return regions.stream().map(region -> new RegionDto(region.getRegionId(), region.getRegionName(), null)).collect(Collectors.toList());
    }

    // 메인 지역 ID에 따라 하위 지역 조회 ( 서울시 -> 강남구 등등 .. 리뷰 작성시 쓰일 메소드)
    public List<SubRegionDto> getSubRegionsByRegion(Long RegionId) {
        List<SubRegion> subRegions = subRegionRepository.findByRegionId(RegionId);
        return subRegions.stream()
                .map(subRegion -> new SubRegionDto(subRegion.getSubRegionId(), subRegion.getSubRegionName()))
                .collect(Collectors.toList());
    }

    // 모든 지역 그룹 반환
    public List<RegionGroupDto> getAllRegionGroups() {
        List<RegionGroup> groups = regionGroupRepository.findAll();
        return groups.stream().map(group -> {
            RegionGroupDto dto = new RegionGroupDto();
            dto.setRegionGroupId(group.getRegionGroupId());
            dto.setRegionGroupName(group.getRegionGroupName());
            dto.setRegions(group.getRegions().stream().map(region -> {
                RegionDto regionDto = new RegionDto();
                regionDto.setRegionId(region.getRegionId());
                regionDto.setRegionName(region.getRegionName());
                // 메인 지역에 속하는 하위 지역만 표시
                regionDto.setSubRegions(region.getSubRegions().stream()
                        .map(subRegion -> new SubRegionDto(subRegion.getSubRegionId(), subRegion.getSubRegionName()))
                        .collect(Collectors.toList()));
                return regionDto;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }
}