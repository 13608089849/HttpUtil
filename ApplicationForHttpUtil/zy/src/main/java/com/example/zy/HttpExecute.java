package com.example.zy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by ZengYu on 2017/5/3.
 */

/**
 * HTTP请求执行类
 */
public class HttpExecute {
    private StringBuffer params = new StringBuffer();
    private StringBuffer responseResult = new StringBuffer();
    private PrintWriter printWriter = null;
    private BufferedReader bufferedReader = null;
    private JSONObject jsonObject = new JSONObject();

    /**
     * 获取HTTP响应
     *
     * @param callBack          回调
     * @param httpUrlConnection HTTP连接
     * @param requestParamsMap  请求参数MAP
     */
    public void getResponse(final CallBack callBack, final HttpURLConnection httpUrlConnection, final Map<String, Object> requestParamsMap) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    // 组织请求参数
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
                    callBack.onBefore(httpUrlConnection.getURL().toString(),jsonObject);
                    httpUrlConnection.connect();
                    OutputStream outputStream = httpUrlConnection.getOutputStream();
                    printWriter = new PrintWriter(outputStream);
                    // 发送请求参数
                    printWriter.write(params.toString());
                    printWriter.flush();
                    int responseCode = httpUrlConnection.getResponseCode();
                    if (responseCode != 200) {
                        callBack.onFailure("ResponseCode:" + responseCode, null);
                    } else {
                        bufferedReader = new BufferedReader(new InputStreamReader(
                                httpUrlConnection.getInputStream(), "utf-8"));
                        String temp;
                        while ((temp = bufferedReader.readLine()) != null) {
                            responseResult.append(temp).append("\r\n");
                        }
                        callBack.onSuccess(responseResult.toString().trim());
                    }
                } catch (IOException e) {
                    callBack.onFailure("", e);
                } catch (JSONException e) {
                    callBack.onFailure("", e);
                } finally {
                    httpUrlConnection.disconnect();
                }
                try {
                    if (printWriter != null) {
                        printWriter.close();
                    }
                    if (bufferedReader != null) {
                        bufferedReader.close();
                    }
                } catch (IOException ex) {
                    callBack.onFailure("", ex);
                }
            }
        }).start();
    }
}
