package com.example.zy;

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
    StringBuffer params = new StringBuffer();
    StringBuffer responseResult = new StringBuffer();
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;

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
                    httpUrlConnection.connect();
                    OutputStream outputStream = httpUrlConnection.getOutputStream();
                    printWriter = new PrintWriter(outputStream);
                    // 组织请求参数
                    Iterator it = requestParamsMap.entrySet().iterator();
                    while (it.hasNext()) {
                        Map.Entry element = (Map.Entry) it.next();
                        params.append(element.getKey());
                        params.append("=");
                        params.append(element.getValue());
                        params.append("&");
                    }
                    if (params.length() > 0) {
                        params.deleteCharAt(params.length() - 1);
                    }
                    // 发送请求参数
                    printWriter.write(params.toString());
                    // flush输出流的缓冲
                    printWriter.flush();
                    // 根据ResponseCode判断连接是否成功
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
