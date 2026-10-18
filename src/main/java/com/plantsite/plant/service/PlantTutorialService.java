package com.plantsite.plant.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.plant.entity.PlantTutorial;
import com.plantsite.plant.mapper.PlantTutorialMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PlantTutorialService extends ServiceImpl<PlantTutorialMapper, PlantTutorial> {

    /** 按步骤序号排序的教程列表 */
    public List<PlantTutorial> listByPlant(Long plantId) {
        return this.list(Wrappers.<PlantTutorial>lambdaQuery()
                .eq(PlantTutorial::getPlantId, plantId)
                .orderByAsc(PlantTutorial::getStepNo).orderByAsc(PlantTutorial::getId));
    }
}
