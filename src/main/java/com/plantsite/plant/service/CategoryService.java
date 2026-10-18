package com.plantsite.plant.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.plant.entity.PlantCategory;
import com.plantsite.plant.mapper.PlantCategoryMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService extends ServiceImpl<PlantCategoryMapper, PlantCategory> {

    /** C 端分类列表（按 sort 排序） */
    public List<PlantCategory> listAll() {
        return this.list(Wrappers.<PlantCategory>lambdaQuery()
                .orderByAsc(PlantCategory::getSort).orderByAsc(PlantCategory::getId));
    }
}
