package com.example.zy;

/**
 * Created by ZengYu on 2017/5/3.
 */

import org.json.JSONObject;

/**
 * 回调
 */
public abstract class HttpCallback {
    /**
     * 成功时返回
     *
     * @param response 服务器返回字符串
     */
    public abstract void onSuccess(String response);

    /**
     * 失败时返回
     *
     * @param e 异常
     */
    public abstract void onFailure(Exception e);

    /**
     * 请求的信息
     *
     * @param url    请求地址
     * @param params 附加参数
     */
    public void onBefore(String url, JSONObject params) {
        System.out.println("Request url:\t" + url);
        System.out.println("Request params:\t" + params.toString());
    }
}
