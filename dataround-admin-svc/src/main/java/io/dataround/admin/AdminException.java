/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin;

/**
 * Admin exception
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public class AdminException extends RuntimeException {

    public AdminException() {
        super();
    }

    public AdminException(String message) {
        super(message);
    }

    public AdminException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdminException(Throwable cause) {
        super(cause);
    }

}
