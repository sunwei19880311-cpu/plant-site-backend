package com.plantsite.plant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName("plant_image")
public class PlantImage {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long plantId;

    /** S3 对象键 */
    private String objectKey;

    private String imgDesc;

    private Integer sort;

    private Boolean isCover;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
