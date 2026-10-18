package com.plantsite.plant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName("plant")
public class Plant {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String alias;

    private String plantCode;

    private String scientificName;

    private String familyGenus;

    private Long categoryId;

    /** 新手 / 中等 / 进阶 */
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

    /** 0=未上架 1=已上架 */
    private Integer status;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
