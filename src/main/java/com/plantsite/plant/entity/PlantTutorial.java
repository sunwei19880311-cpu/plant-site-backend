package com.plantsite.plant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName("plant_tutorial")
public class PlantTutorial {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long plantId;

    /** 步骤序号 */
    private Integer stepNo;

    private String stepTitle;

    private String stepContent;

    private String objectKey;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
