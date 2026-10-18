package com.plantsite.plant.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.plant.entity.PlantImage;
import com.plantsite.plant.mapper.PlantImageMapper;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class PlantImageService extends ServiceImpl<PlantImageMapper, PlantImage> {

    /** 每个植物的封面 objectKey（无封面则取 sort 最小的一张），用于 C 端列表 */
    public Map<Long, String> coverKeyMap(List<Long> plantIds) {
        if (CollectionUtils.isEmpty(plantIds)) {
            return Map.of();
        }
        List<PlantImage> images = this.list(Wrappers.<PlantImage>lambdaQuery()
                .in(PlantImage::getPlantId, plantIds)
                .orderByAsc(PlantImage::getSort).orderByAsc(PlantImage::getId));
        return images.stream().collect(Collectors.toMap(
                PlantImage::getPlantId,
                PlantImage::getObjectKey,
                (first, second) -> first));
    }
}
