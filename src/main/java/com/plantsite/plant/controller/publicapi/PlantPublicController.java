package com.plantsite.plant.controller.publicapi;

import com.plantsite.common.PageResult;
import com.plantsite.common.R;
import com.plantsite.plant.service.PlantPublicService;
import com.plantsite.plant.vo.PlantCardVO;
import com.plantsite.plant.vo.PlantDetailVO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * C 端公开接口：植物列表 / 详情 / 热门推荐（无需登录）
 */
@RestController
@RequestMapping("/api/plant")
@RequiredArgsConstructor
public class PlantPublicController {

    private final PlantPublicService plantPublicService;

    /** 植物分页（keyword 模糊搜索名称/别名/搜索标签；categoryId 分类；difficulty 难度） */
    @GetMapping("/page")
    public R<PageResult<PlantCardVO>> page(@RequestParam(defaultValue = "1") long pageNum,
                                           @RequestParam(defaultValue = "10") long pageSize,
                                           @RequestParam(required = false) String keyword,
                                           @RequestParam(required = false) Long categoryId,
                                           @RequestParam(required = false) String difficulty) {
        return R.ok(plantPublicService.page(pageNum, pageSize, keyword, categoryId, difficulty));
    }

    /** 首页热门植物 */
    @GetMapping("/hot")
    public R<List<PlantCardVO>> hot(@RequestParam(defaultValue = "6") int limit) {
        return R.ok(plantPublicService.hot(limit));
    }

    /** 植物详情（聚合图片/教程/常见问题/商品推荐，图片为预签名 URL） */
    @GetMapping("/{id}")
    public R<PlantDetailVO> detail(@PathVariable Long id) {
        return R.ok(plantPublicService.detail(id));
    }
}
