package com.example.agentzengyu.applicationforhttputil;

import android.util.Log;

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

public class HttpExecute {
    StringBuffer params = new StringBuffer();
    StringBuffer responseResult = new StringBuffer();
    PrintWriter printWriter = null;
    BufferedReader bufferedReader = null;
    String responseString = "";

    public void getResponse(CallBack callBack, HttpURLConnection httpUrlConnection, Map<String, Object> requestParamsMap) {
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
                Log.e("Error", responseCode + "");
            } else {
                callBack.onFailure("Response Code: " + responseCode, null);
                Log.e("Post Success", "");
            }
            bufferedReader = new BufferedReader(new InputStreamReader(
                    httpUrlConnection.getInputStream()));

            while ((responseString = bufferedReader.readLine()) != null) {
                responseResult.append("/n").append(responseString);
            }
            callBack.onSuccess(responseString);
        } catch (IOException e) {
            callBack.onFailure("", e);
            e.printStackTrace();
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
            ex.printStackTrace();
        }
    }
}
