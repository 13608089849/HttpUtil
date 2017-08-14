package com.example.zy;

import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZengYu on 2017/5/3.
 */

/**
 * HTTP请求执行类
 */
public final class HttpExecute {
    private String urlString = "";
    private int CONNECT_TIME_OUT = 20000;
    private int READ_TIME_OUT = 20000;
    private Map<String, Object> requestParamsMap = new HashMap<>();

    private HttpExecute() {
    }

    public HttpExecute(String url) {
        if (url != null) {
            urlString = url;
        }
    }

    /**
     * 设置连接超时
     *
     * @param time 连接超时
     * @return 返回实例本身
     */
    public HttpExecute setConnectTimeOut(int time) {
        this.CONNECT_TIME_OUT = time;
        return this;
    }

    /**
     * 设置读取超时
     *
     * @param time 读取超时
     * @return 返回实例本身
     */
    public HttpExecute setReadTimeOut(int time) {
        this.READ_TIME_OUT = time;
        return this;
    }

    /**
     * 设置发送参数
     *
     * @param key   参数键
     * @param value 参数值
     * @return 返回实例本身
     */
    public HttpExecute addParams(String key, Object value) {
        requestParamsMap.put(key, value);
        return this;
    }

    /**
     * 请求执行入口
     *
     * @param callback
     */
    public void execute(@NonNull HttpCallback callback) {
        try {
            if (callback == null) {
                throw new HttpException.NullCallBackException("Lacking a callback to execute request.");
            }
            if (urlString.equals("")) {
                throw new HttpException.NullUrlException("The url is not allowed to be empty.");
            }
            URL url = new URL(urlString);
            URLConnection urlConnection = url.openConnection();
            HttpURLConnection httpUrlConnection = (HttpURLConnection) urlConnection;
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
            getResponse(callback, httpUrlConnection, requestParamsMap);
        } catch (HttpException.NullCallBackException e) {
            e.printStackTrace();
        } catch (HttpException.NullUrlException e) {
            callback.onFailure(e);
        } catch (IOException e) {
            callback.onFailure(e);
        }
    }

    /**
     * 获取HTTP响应
     *
     * @param callback          回调
     * @param httpUrlConnection HTTP连接
     * @param requestParamsMap  请求参数MAP
     */
    private void getResponse(final HttpCallback callback, final HttpURLConnection httpUrlConnection, final Map<String, Object> requestParamsMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter printWriter = null;
                BufferedReader bufferedReader = null;
                try {
                    // 组织请求参数
                    StringBuffer params = new StringBuffer();
                    JSONObject jsonObject = new JSONObject();
                    Iterator it = requestParamsMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry element = (Map.Entry) it.next();
                        params.append(element.getKey());
                        params.append("=");
                        params.append(element.getValue());
                        params.append("&");
                        jsonObject.put(element.getKey() + "", element.getValue() + "");
                    }
                    if (params.length() > 0) {
                        params.deleteCharAt(params.length() - 1);
                    }
                    callback.onBefore(httpUrlConnection.getURL().toString(), jsonObject);
                    httpUrlConnection.connect();
                    OutputStream outputStream = httpUrlConnection.getOutputStream();
                    printWriter = new PrintWriter(outputStream);
                    // 发送请求参数
                    printWriter.write(params.toString());
                    printWriter.flush();
                    int responseCode = httpUrlConnection.getResponseCode();
                    if (responseCode != 200) {
                        throw new HttpException.ResponseCodeException("Response Code is " + responseCode);
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(
                                httpUrlConnection.getInputStream(), "utf-8"));
                        StringBuffer responseResult = new StringBuffer();
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            responseResult.append(temp).append("\r\n");
                        }
                        callback.onSuccess(responseResult.toString().trim());
                    }
                } catch (IOException e) {
                    callback.onFailure(e);
                } catch (JSONException e) {
                    callback.onFailure(e);
                } catch (HttpException.ResponseCodeException e) {
                    callback.onFailure(e);
                } finally {
                    try {
                        if (printWriter != null) {
                            printWriter.close();
                        }
                        if (bufferedReader != null) {
                            bufferedReader.close();
                        }
                    } catch (IOException ex) {
                        callback.onFailure(ex);
                    }
                    httpUrlConnection.disconnect();
                }
            }
        }).start();
    }
}
