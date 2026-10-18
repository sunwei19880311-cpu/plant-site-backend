package com.plantsite.plant.vo;

import lombok.Data;

/** C 端植物卡片 VO */
@Data
public class PlantCardVO {

    private Long id;

    private String name;

    /** 分类名称 */
    private String category;

    /** 新手 / 中等 / 进阶 */
    private String difficulty;

    /** 逗号分隔标签 */
    private String tags;

    /** 简介摘要 */
    private String summary;

    private String temperature;

    /** 封面图（S3 预签名 URL） */
    private String imageUrl;
}
