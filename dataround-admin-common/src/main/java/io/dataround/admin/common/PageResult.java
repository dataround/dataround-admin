/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */
package io.dataround.admin.common;

/**
 * PageResult
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public class PageResult<T> extends Result<T> {
    private long total;

    public PageResult() {
    }

    public static <T> PageResult<T> success(long total, T data) {
        PageResult<T> pageResult = new PageResult<>();
        pageResult.setTotal(total);
        pageResult.setData(data);
        return pageResult;
    }

    public long getTotal() {
        return total;
    }

    public void setTotal(long total) {
        this.total = total;
    }
}
