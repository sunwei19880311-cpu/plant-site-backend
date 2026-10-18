package com.plantsite.plant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName("plant_category")
public class PlantCategory {

    @TableId(type = IdType.AUTO)
    private Long id;

    private String name;

    private String categoryDesc;

    private String icon;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
