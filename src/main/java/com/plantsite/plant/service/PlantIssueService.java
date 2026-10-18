package com.plantsite.plant.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.plant.entity.PlantIssue;
import com.plantsite.plant.mapper.PlantIssueMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantIssueService extends ServiceImpl<PlantIssueMapper, PlantIssue> {

    /** 植物常见故障问题列表 */
    public List<PlantIssue> listByPlant(Long plantId) {
        return this.list(Wrappers.<PlantIssue>lambdaQuery()
                .eq(PlantIssue::getPlantId, plantId)
                .orderByAsc(PlantIssue::getSort).orderByAsc(PlantIssue::getId));
    }
}
