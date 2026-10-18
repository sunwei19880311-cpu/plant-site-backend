package com.plantsite.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 统一响应包装 R{code, msg, data}，约定 code=0 为成功。
 * 与前端 plant-site-admin/src/api/http.ts 的 ApiResp 契约一致。
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class R<T> {

    public static final int CODE_SUCCESS = 0;
    public static final int CODE_BAD_REQUEST = 400;
    public static final int CODE_UNAUTHORIZED = 401;
    public static final int CODE_NOT_FOUND = 404;
    public static final int CODE_ERROR = 500;

    private int code;
    private String msg;
    private T data;

    public static <T> R<T> ok(T data) {
        return new R<>(CODE_SUCCESS, "ok", data);
    }

    public static R<Void> ok() {
        return new R<>(CODE_SUCCESS, "ok", null);
    }

    public static <T> R<T> error(int code, String msg) {
        return new R<>(code, msg, null);
    }
}
