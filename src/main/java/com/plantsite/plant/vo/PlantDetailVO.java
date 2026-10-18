package com.plantsite.plant.vo;

import lombok.Data;

import java.util.List;

/**
 * C 端植物详情 VO：植物主信息 + 图片 + 养护教程 + 常见问题 + CPS 商品推荐。
 * 图片 URL 均为 S3 预签名临时地址。
 */
@Data
public class PlantDetailVO {

    private Long id;

    private String name;

    private String alias;

    private String plantCode;

    private String scientificName;

    private String familyGenus;

    private Long categoryId;

    /** 分类名称 */
    private String category;

    private String difficulty;

    private String tags;

    private String summary;

    private String lightDesc;

    private String waterDesc;

    private String soilDesc;

    private String tempDesc;

    private String fertilizeDesc;

    private String pestDesc;

    private String propagateDesc;

    private String poemContent;

    private String poemExplain;

    private String searchTags;

    private String meaning;

    /** 封面大图（S3 预签名 URL） */
    private String imageUrl;

    private List<ImageItem> images;

    private List<TutorialItem> tutorials;

    private List<IssueItem> commonProblems;

    private List<ProductItem> goods;

    @Data
    public static class ImageItem {
        private Long id;
        private String objectKey;
        private String url;
        private String imgDesc;
        private Boolean isCover;
    }

    @Data
    public static class TutorialItem {
        private Long id;
        private Integer stepNo;
        private String stepTitle;
        private String stepContent;
        private String imgUrl;
    }

    @Data
    public static class IssueItem {
        private Long id;
        private String symptom;
        private String phenomenon;
        private String reason;
        private String solution;
    }

    @Data
    public static class ProductItem {
        private Long id;
        private String title;
        private String productDesc;
        private String price;
        private String imageUrl;
        private String linkUrl;
        private String platform;
    }
}
