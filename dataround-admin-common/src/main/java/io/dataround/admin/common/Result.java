/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.common;

import com.fasterxml.jackson.annotation.JsonInclude;

/**
 * Result
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class Result<T> {

    public static final int STATUS_OK = 200;
    public static final int STATUS_ERR = 500;
    protected int code = STATUS_OK;
    protected T data;
    protected String msg;

    public Result() {
    }

    private Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    private Result(int code, T data, String msg) {
        this.code = code;
        this.data = data;
        this.msg = msg;
    }

    public static <T> Result<T> success() {
        return new Result<>(STATUS_OK, null, null);
    }

    public static <T> Result<T> success(T data) {
        return new Result<>(STATUS_OK, data, null);
    }

    public static <T> Result<T> success(String msg) {
        return new Result<>(STATUS_OK, msg);
    }

    public static <T> Result<T> error() {
        return new Result<>(STATUS_ERR, null, null);
    }

    public static <T> Result<T> error(String msg) {
        return new Result<>(STATUS_ERR, msg);
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
