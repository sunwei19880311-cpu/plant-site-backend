package com.plantsite.plant.controller.publicapi;

import com.plantsite.common.R;
import com.plantsite.plant.entity.PlantCategory;
import com.plantsite.plant.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * C 端公开接口：分类列表（首页快捷入口 / 知识库筛选）
 */
@RestController
@RequestMapping("/api/category")
@RequiredArgsConstructor
public class CategoryPublicController {

    private final CategoryService categoryService;

    @GetMapping("/list")
    public R<List<PlantCategory>> list() {
        return R.ok(categoryService.listAll());
    }
}
