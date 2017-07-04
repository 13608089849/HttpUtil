package com.example.zy;

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
 * HTTP工具类（POST），支持字符串类型数据发送
 */
public class HttpUtil {
    private HttpExecute httpExecute = new HttpExecute();
    private String urlString = "";
    private int CONNECT_TIME_OUT = 20000;
    private int READ_TIME_OUT = 20000;
    private Map<String, Object> requestParamsMap = new HashMap<>();
    private HttpURLConnection httpUrlConnection = null;

    /**
     * 设置请求地址
     *
     * @param urlString 请求地址
     * @return 返回类本身
     */
    public HttpUtil setUrl(String urlString) {
        this.urlString = urlString;
        return this;
    }

    /**
     * 设置连接超时
     *
     * @param time 连接超时
     * @return 返回类本身
     */
    public HttpUtil setConnectTimeOut(int time) {
        this.CONNECT_TIME_OUT = time;
        return this;
    }

    /**
     * 设置读取超时
     *
     * @param time 读取超时
     * @return 返回类本身
     */
    public HttpUtil setReadTimeOut(int time) {
        this.READ_TIME_OUT = time;
        return this;
    }

    /**
     * 设置发送参数
     *
     * @param key   参数键
     * @param value 参数值
     * @return 返回类本身
     */
    public HttpUtil addParams(String key, Object value) {
        requestParamsMap.put(key, value);
        return this;
    }

    /**
     * 请求执行入口
     *
     * @param callback 回调
     */
    public void execute(HttpCallback callback) {
        try {
            if (urlString.equals("")) {
                callback.onFailure("Url is empty!", null);
                return;
            }
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            httpUrlConnection = (HttpURLConnection) urlConnection;
            //是否向httpUrlConnection输出
            httpUrlConnection.setDoOutput(true);
            //是否从httpUrlConnection读入
            httpUrlConnection.setDoInput(true);
            httpUrlConnection.setConnectTimeout(CONNECT_TIME_OUT);
            httpUrlConnection.setReadTimeout(READ_TIME_OUT);
            httpUrlConnection.setRequestMethod("POST");
            //是否使用缓存
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("accept", "*/*");
            httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            httpExecute.getResponse(callback, httpUrlConnection, requestParamsMap);
        } catch (IOException e) {
            callback.onFailure("IOException in execute", e);
        }
    }
}