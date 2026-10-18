package com.plantsite.cps.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Data
@TableName("cps_product")
public class CpsProduct {

    @TableId(type = IdType.AUTO)
    private Long id;

    /** 绑定植物 ID，null = 全站通用商品 */
    private Long plantId;

    private String title;

    private String productDesc;

    private String objectKey;

    private String linkUrl;

    private String platform;

    private BigDecimal price;

    /** 佣金率 %，如 12.50 */
    private BigDecimal commissionRate;

    /** 联盟原始商品 ID（淘宝 itemId） */
    private String originItemId;

    private Integer sort;

    /** 0=下架 1=上架 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
