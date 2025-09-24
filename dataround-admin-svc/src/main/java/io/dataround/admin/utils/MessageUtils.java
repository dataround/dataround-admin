/**
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

 package io.dataround.admin.utils;

import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

/**
 * Message utils
 *
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
@Component
public class MessageUtils {

    private static MessageSource messageSource;

    public MessageUtils(MessageSource messageSource) {
        MessageUtils.messageSource = messageSource;
    }

    /**
     * Get internationalized message
     *
     * @param code Message code
     * @return Internationalized message
     */
    public static String getMessage(String code) {
        return getMessage(code, null);
    }

    /**
     * Get internationalized message
     *
     * @param code Message code
     * @param args Parameters
     * @return Internationalized message
     */
    public static String getMessage(String code, Object[] args) {
        return messageSource.getMessage(code, args, code, LocaleContextHolder.getLocale());
    }
}