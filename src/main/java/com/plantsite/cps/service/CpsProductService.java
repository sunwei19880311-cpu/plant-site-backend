package com.plantsite.cps.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.plantsite.cps.entity.CpsProduct;
import com.plantsite.cps.mapper.CpsProductMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CpsProductService extends ServiceImpl<CpsProductMapper, CpsProduct> {

    /** C 端：指定植物的商品 + 全站通用商品（plant_id 为空），已上架，按 sort 排序；plantId 为空时只返回前 5 条 */
    public List<CpsProduct> listOnShelf(Long plantId, Integer limit) {
        if (plantId == null) {
            limit = 5;
        }
        var wrapper = Wrappers.<CpsProduct>lambdaQuery()
                .eq(CpsProduct::getStatus, 1)
                .and(plantId != null, w -> w.eq(CpsProduct::getPlantId, plantId).or().isNull(CpsProduct::getPlantId))
                .and(plantId == null, w -> w.isNull(CpsProduct::getPlantId))
                .orderByAsc(CpsProduct::getSort).orderByAsc(CpsProduct::getId);
        List<CpsProduct> list = this.list(wrapper);
        if (limit != null && limit > 0 && list.size() > limit) {
            list = list.subList(0, limit);
        }
        return list;
    }
}
