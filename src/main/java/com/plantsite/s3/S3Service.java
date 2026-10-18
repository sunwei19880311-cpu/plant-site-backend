package com.plantsite.s3;

import com.plantsite.common.BizException;
import com.plantsite.config.S3Properties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

/**
 * AWS S3 文件上传与预签名。
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class S3Service {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final S3Properties props;

    /**
     * 上传文件到 S3，返回 object_key。
     * 键规则：{dir}/{yyyyMMdd}/{uuid}.{ext}
     */
    public String upload(MultipartFile file, String dir) {
        if (file == null || file.isEmpty()) {
            throw new BizException(400, "上传文件不能为空");
        }
        String safeDir = sanitizeDir(dir);
        String ext = extension(file.getOriginalFilename());
        String key = safeDir + "/" + LocalDate.now().format(DateTimeFormatter.BASIC_ISO_DATE)
                + "/" + UUID.randomUUID().toString().replace("-", "") + "." + ext;

        PutObjectRequest request = PutObjectRequest.builder()
                .bucket(props.getBucket())
                .key(key)
                .contentType(file.getContentType() != null ? file.getContentType() : "application/octet-stream")
                .contentLength(file.getSize())
                .build();
        try {
            s3Client.putObject(request, RequestBody.fromInputStream(file.getInputStream(), file.getSize()));
        } catch (IOException e) {
            log.error("S3 上传失败", e);
            throw new BizException(500, "文件上传失败");
        }
        return key;
    }

    /**
     * 生成 GET 预签名临时访问 URL。
     */
    public String presignGet(String objectKey) {
        GetObjectRequest get = GetObjectRequest.builder()
                .bucket(props.getBucket())
                .key(objectKey)
                .build();
        GetObjectPresignRequest presign = GetObjectPresignRequest.builder()
                .signatureDuration(Duration.ofMinutes(props.getPresignExpireMinutes()))
                .getObjectRequest(get)
                .build();
        return s3Presigner.presignGetObject(presign).url().toString();
    }

    /**
     * 安全版预签名：objectKey 为空返回 null，S3 异常时降级返回 null 不影响主流程。
     */
    public String presignGetSafe(String objectKey) {
        if (!StringUtils.hasText(objectKey)) {
            return null;
        }
        try {
            return presignGet(objectKey);
        } catch (Exception e) {
            log.warn("生成预签名 URL 失败, objectKey={}: {}", objectKey, e.getMessage());
            return null;
        }
    }

    /** 目录白名单化：去空白、限制路径穿越，空则用默认目录 */
    private String sanitizeDir(String dir) {
        String d = dir;
        if (!StringUtils.hasText(d)) {
            d = props.getDefaultDir();
        }
        d = d.trim()
                .replace("\\", "/")
                .replaceAll("\\.+", "")
                .replaceAll("^/+|/+$", "");
        if (!StringUtils.hasText(d)) {
            d = props.getDefaultDir();
        }
        return d;
    }

    private String extension(String filename) {
        if (filename != null && filename.contains(".")) {
            String ext = filename.substring(filename.lastIndexOf('.') + 1).toLowerCase();
            if (ext.matches("[a-z0-9]{1,8}")) {
                return ext;
            }
        }
        return "bin";
    }
}
