package com.plantsite.plant.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.plant.entity.Plant;
import com.plantsite.plant.mapper.PlantMapper;
import org.springframework.stereotype.Service;

@Service
public class PlantService extends ServiceImpl<PlantMapper, Plant> {
}
