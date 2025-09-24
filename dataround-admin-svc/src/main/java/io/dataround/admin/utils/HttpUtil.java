/*
 * Copyright (C) 2025 yuehan124@gmail.com
 *
 * This repository is licensed under the Dataround Open Source License
 */

package io.dataround.admin.utils;

import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import java.io.IOException;
import java.util.Map;

/**
 * Http util
 * 
 * @author yuehan124@gmail.com
 * @since 2025/09/21
 */
public class HttpUtil {
    private static OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json");

    public static String get(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String post(String url, String json) throws IOException {
        RequestBody body = RequestBody.create(json, JSON);
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }

    public static String postForm(String url, Map<String, String> map) throws IOException {
        FormBody.Builder builder = new FormBody.Builder();
        for (String key : map.keySet()) {
            builder.add(key, map.get(key));
        }
        RequestBody body = builder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            return response.body().string();
        }
    }
}
