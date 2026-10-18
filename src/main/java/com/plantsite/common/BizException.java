package com.plantsite.common;

import lombok.Getter;

/**
 * 业务异常，携带响应 code 与提示信息。
 */
@Getter
public class BizException extends RuntimeException {

    private final int code;

    public BizException(String msg) {
        this(R.CODE_ERROR, msg);
    }

    public BizException(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public static BizException notFound(String msg) {
        return new BizException(R.CODE_NOT_FOUND, msg);
    }
}
