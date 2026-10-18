package com.plantsite.cps.vo;

import lombok.Data;

/** C 端 CPS 商品卡片 VO */
@Data
public class CpsProductCardVO {

    private Long id;

    private String title;

    private String productDesc;

    /** 展示价格（格式化字符串，如 "¥49.00"） */
    private String price;

    /** 商品图（S3 预签名 URL） */
    private String imageUrl;

    private String linkUrl;

    private String platform;
}
