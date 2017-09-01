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
public final class HttpUtil {
    private String urlString = "";
    private int CONNECT_TIME_OUT = 20000;
    private int READ_TIME_OUT = 20000;
    private String REQUEST_METHOD = "POST";
    private HttpCallback callback = null;
    private Map<String, Object> requestParamsMap = new HashMap<>();
    private StringBuffer params = null;

    /**
     * 无参构造
     */
    public HttpUtil() {
    }

    /**
     * 含参构造
     *
     * @param url 请求地址
     */
    public HttpUtil(String url) {
        if (url != null) {
            urlString = url;
        }
    }

    /**
     * 设置请求地址
     *
     * @param url 请求地址
     * @return
     */
    public HttpUtil setUrl(@NonNull String url) {
        if (url != null) {
            urlString = url;
        }
        return this;
    }

    /**
     * 设置请求方法
     *
     * @param method 请求方法
     * @return
     */
    public HttpUtil setRequestMethod(@IRequestMethod String method) {
        if (method != null) {
            REQUEST_METHOD = method.toUpperCase();
        }
        return this;
    }

    /**
     * 设置连接超时
     *
     * @param time 连接超时
     * @return 返回实例本身
     */
    public HttpUtil setConnectTimeOut(int time) {
        this.CONNECT_TIME_OUT = time;
        return this;
    }

    /**
     * 设置读取超时
     *
     * @param time 读取超时
     * @return 返回实例本身
     */
    public HttpUtil setReadTimeOut(int time) {
        this.READ_TIME_OUT = time;
        return this;
    }

    /**
     * 添加附加参数
     *
     * @param key   参数键
     * @param value 参数值
     * @return 返回实例本身
     */
    public HttpUtil addParams(String key, byte value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, short value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, int value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, long value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, float value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, double value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, char value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, boolean value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, String value) {
        requestParamsMap.put(key, value);
        return this;
    }

    public HttpUtil addParams(String key, JSONObject value) {
        requestParamsMap.put(key, value);
        return this;
    }

    /**
     * 请求执行入口
     *
     * @param callback 请求回调
     */
    public void execute(@NonNull HttpCallback callback) {
        try {
            if (callback == null) {
                throw new HttpException.NullCallBackException("Lacking a callback to execute request.");
            }else {
                this.callback = callback;
            }
            if (urlString.equals("")) {
                throw new HttpException.NullUrlException("The url is not allowed to be empty.");
            }
            if (!(REQUEST_METHOD.equals("POST") || REQUEST_METHOD.equals("GET"))) {
                throw new HttpException.RequestMethodException("Unknown request method.");
            }

            // 组织请求参数
            params = new StringBuffer();
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
            callback.onBefore(urlString, jsonObject);
            if (REQUEST_METHOD.equals("GET")) {
                urlString += ("?" + params.toString());
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
            httpUrlConnection.setRequestMethod(REQUEST_METHOD);
            //是否使用缓存
            httpUrlConnection.setUseCaches(false);
            httpUrlConnection.setRequestProperty("accept", "*/*");
            httpUrlConnection.setRequestProperty("connection", "Keep-Alive");
            httpUrlConnection.setRequestProperty("Content-type", "application/x-www-form-urlencoded");
            httpUrlConnection.setRequestProperty("Accept-Charset", "UTF-8");
            getResponse(httpUrlConnection);
        } catch (HttpException.NullCallBackException e) {
            callback.onFailure(e);
        } catch (HttpException.NullUrlException e) {
            callback.onFailure(e);
        } catch (HttpException.RequestMethodException e) {
            callback.onFailure(e);
        } catch (JSONException e) {
            callback.onFailure(e);
        } catch (IOException e) {
            callback.onFailure(e);
        }
    }

    /**
     * 获取HTTP响应
     *
     * @param httpUrlConnection HTTP连接
     */
    private void getResponse(final HttpURLConnection httpUrlConnection) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                PrintWriter printWriter = null;
                BufferedReader bufferedReader = null;
                try {
                    httpUrlConnection.connect();
                    if (REQUEST_METHOD.equals(IRequestMethod.POST)) {
                        OutputStream outputStream = httpUrlConnection.getOutputStream();
                        printWriter = new PrintWriter(outputStream);
                        // 发送请求参数
                        printWriter.write(params.toString());
                        printWriter.flush();
                        outputStream.close();
                    }

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
