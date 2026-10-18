package com.plantsite.plant.service;

import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.plantsite.common.BizException;
import com.plantsite.common.PageResult;
import com.plantsite.cps.entity.CpsProduct;
import com.plantsite.cps.service.CpsProductService;
import com.plantsite.plant.entity.Plant;
import com.plantsite.plant.entity.PlantCategory;
import com.plantsite.plant.entity.PlantImage;
import com.plantsite.plant.vo.PlantCardVO;
import com.plantsite.plant.vo.PlantDetailVO;
import com.plantsite.s3.S3Service;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * C 端公开查询：植物卡片分页 / 详情聚合（图片、教程、故障、商品）。
 * 所有图片字段均为 S3 预签名临时 URL。
 */
@Service
@RequiredArgsConstructor
public class PlantPublicService {

    private final PlantService plantService;
    private final CategoryService categoryService;
    private final PlantImageService plantImageService;
    private final PlantTutorialService plantTutorialService;
    private final PlantIssueService plantIssueService;
    private final CpsProductService cpsProductService;
    private final S3Service s3Service;

    /** 植物卡片分页（仅已上架） */
    public PageResult<PlantCardVO> page(long pageNum, long pageSize, String keyword,
                                        Long categoryId, String difficulty) {
        var wrapper = Wrappers.<Plant>lambdaQuery().eq(Plant::getStatus, 1);
        if (StringUtils.hasText(keyword)) {
            wrapper.and(w -> w.like(Plant::getName, keyword)
                    .or().like(Plant::getAlias, keyword)
                    .or().like(Plant::getSearchTags, keyword));
        }
        if (categoryId != null) {
            wrapper.eq(Plant::getCategoryId, categoryId);
        }
        if (StringUtils.hasText(difficulty)) {
            wrapper.eq(Plant::getDifficulty, difficulty);
        }
        wrapper.orderByAsc(Plant::getId);

        com.baomidou.mybatisplus.extension.plugins.pagination.Page<Plant> mpPage =
                plantService.page(new com.baomidou.mybatisplus.extension.plugins.pagination.Page<>(pageNum, pageSize), wrapper);
        PageResult<Plant> page = PageResult.from(mpPage, p -> p);

        List<Long> plantIds = page.getRecords().stream().map(Plant::getId).collect(Collectors.toList());
        Map<Long, String> coverKeys = plantImageService.coverKeyMap(plantIds);
        Map<Long, String> categoryNames = categoryNamesOf(page.getRecords());

        List<PlantCardVO> cardList = page.getRecords().stream().map(p -> {
            PlantCardVO vo = new PlantCardVO();
            vo.setId(p.getId());
            vo.setName(p.getName());
            vo.setCategory(categoryNames.get(p.getCategoryId()));
            vo.setDifficulty(p.getDifficulty());
            vo.setTags(p.getTags());
            vo.setSummary(p.getSummary());
            vo.setTemperature(p.getTempDesc());
            vo.setImageUrl(s3Service.presignGetSafe(coverKeys.get(p.getId())));
            return vo;
        }).collect(Collectors.toList());
        return new PageResult<>(cardList, page.getPageNum(), page.getPageSize(), page.getTotal(), page.getTotalPages());
    }

    /** 首页热门植物（取前 N 个已上架） */
    public List<PlantCardVO> hot(int limit) {
        return page(1, Math.min(Math.max(limit, 1), 50), null, null, null).getRecords();
    }

    /** 植物详情聚合 */
    public PlantDetailVO detail(Long id) {
        Plant plant = plantService.getById(id);
        if (plant == null || !Integer.valueOf(1).equals(plant.getStatus())) {
            throw BizException.notFound("植物不存在或未上架");
        }

        PlantDetailVO vo = new PlantDetailVO();
        BeanUtils.copyProperties(plant, vo);
        PlantCategory category = plant.getCategoryId() == null ? null : categoryService.getById(plant.getCategoryId());
        vo.setCategory(category == null ? null : category.getName());

        // 封面大图：优先 is_cover，其次 sort 最小
        String coverKey = plantImageService.coverKeyMap(List.of(id)).get(id);
        vo.setImageUrl(s3Service.presignGetSafe(coverKey));

        // 图片列表
        vo.setImages(plantImageService.list(Wrappers.<PlantImage>lambdaQuery()
                        .eq(PlantImage::getPlantId, id)
                        .orderByAsc(PlantImage::getSort).orderByAsc(PlantImage::getId))
                .stream().map(img -> {
                    PlantDetailVO.ImageItem item = new PlantDetailVO.ImageItem();
                    item.setId(img.getId());
                    item.setObjectKey(img.getObjectKey());
                    item.setUrl(s3Service.presignGetSafe(img.getObjectKey()));
                    item.setImgDesc(img.getImgDesc());
                    item.setIsCover(img.getIsCover());
                    return item;
                }).collect(Collectors.toList()));

        // 养护教程分步
        vo.setTutorials(plantTutorialService.listByPlant(id).stream().map(t -> {
            PlantDetailVO.TutorialItem item = new PlantDetailVO.TutorialItem();
            item.setId(t.getId());
            item.setStepNo(t.getStepNo());
            item.setStepTitle(t.getStepTitle());
            item.setStepContent(t.getStepContent());
            item.setImgUrl(s3Service.presignGetSafe(t.getObjectKey()));
            return item;
        }).collect(Collectors.toList()));

        // 常见问题
        vo.setCommonProblems(plantIssueService.listByPlant(id).stream().map(i -> {
            PlantDetailVO.IssueItem item = new PlantDetailVO.IssueItem();
            item.setId(i.getId());
            item.setSymptom(i.getSymptom());
            item.setPhenomenon(i.getPhenomenon());
            item.setReason(i.getReason());
            item.setSolution(i.getSolution());
            return item;
        }).collect(Collectors.toList()));

        // CPS 商品推荐（本植物 + 全站通用）
        vo.setGoods(cpsProductService.listOnShelf(id, 10).stream().map(this::toProductItem)
                .collect(Collectors.toList()));
        return vo;
    }

    private PlantDetailVO.ProductItem toProductItem(CpsProduct p) {
        PlantDetailVO.ProductItem item = new PlantDetailVO.ProductItem();
        item.setId(p.getId());
        item.setTitle(p.getTitle());
        item.setProductDesc(p.getProductDesc());
        item.setPrice(formatPrice(p.getPrice()));
        item.setImageUrl(s3Service.presignGetSafe(p.getObjectKey()));
        item.setLinkUrl(p.getLinkUrl());
        item.setPlatform(p.getPlatform());
        return item;
    }

    private Map<Long, String> categoryNamesOf(List<Plant> plants) {
        List<Long> categoryIds = plants.stream().map(Plant::getCategoryId)
                .filter(java.util.Objects::nonNull).distinct().collect(Collectors.toList());
        if (categoryIds.isEmpty()) {
            return Map.of();
        }
        return categoryService.listByIds(categoryIds).stream()
                .collect(Collectors.toMap(PlantCategory::getId, PlantCategory::getName, (a, b) -> a));
    }

    private String formatPrice(BigDecimal price) {
        if (price == null) {
            return null;
        }
        return "¥" + price.stripTrailingZeros().toPlainString();
    }
}
