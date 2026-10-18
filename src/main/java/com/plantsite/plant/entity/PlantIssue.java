package com.plantsite.plant.entity;

import com.baomidou.mybatisplus.annotation.*;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
@TableName("plant_issue")
public class PlantIssue {

    @TableId(type = IdType.AUTO)
    private Long id;

    private Long plantId;

    /** 症状，如 黄叶/烂根 */
    private String symptom;

    /** 现象描述 */
    private String phenomenon;

    /** 原因 */
    private String reason;

    /** 解决方案 */
    private String solution;

    private Integer sort;

    @TableField(fill = FieldFill.INSERT)
    private OffsetDateTime createdAt;

    @TableField(fill = FieldFill.INSERT_UPDATE)
    private OffsetDateTime updatedAt;

    @TableLogic(value = "null", delval = "NOW()")
    private OffsetDateTime deletedAt;
}
