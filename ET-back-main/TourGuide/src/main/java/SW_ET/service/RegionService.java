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

    
    // 헤더에 지역 정보 동적표시 메소드
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
                regionDto.setSubRegions(region.getSubRegions().stream()
                        .map(subRegion -> new SubRegionDto(subRegion.getSubRegionId(), subRegion.getSubRegionName()))
                        .collect(Collectors.toList()));
                return regionDto;
            }).collect(Collectors.toList()));
            return dto;
        }).collect(Collectors.toList());
    }

    
// region, sub_region 선택 메소드
    public List<RegionDto> getAllRegions() {
        List<Region> regions = regionRepository.findAll();
        return regions.stream().map(region -> new RegionDto(
                region.getRegionId(),
                region.getRegionName(),
                region.getSubRegions().stream()
                        .map(subRegion -> new SubRegionDto(subRegion.getSubRegionId(), subRegion.getSubRegionName()))
                        .collect(Collectors.toList())
        )).collect(Collectors.toList());
    }
}