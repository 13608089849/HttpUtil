package com.example.zy;

import android.support.annotation.NonNull;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ZengYu on 2017/5/3.
 */

/**
 * HTTP工具类（POST），支持字符串类型Json数据发送
 */
public final class HttpUtil {
    private HttpUtil() {
    }

    /**
     * 设置请求地址
     *
     * @param url
     * @return
     */
    public static HttpExecute setUrl(@NonNull String url) {
        return new HttpExecute(url);
    }
}