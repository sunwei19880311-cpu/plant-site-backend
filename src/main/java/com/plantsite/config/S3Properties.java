package com.plantsite.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * AWS S3 配置项（application.yml -> aws.s3）。
 */
@Data
@Component
@ConfigurationProperties(prefix = "aws.s3")
public class S3Properties {

    private String region = "us-east-1";
    private String bucket;
    /** 留空则走 AWS 默认凭证链 */
    private String accessKey;
    private String secretKey;
    /** 预签名 URL 有效期（分钟） */
    private int presignExpireMinutes = 60;
    /** 上传默认目录 */
    private String defaultDir = "picture";
}
