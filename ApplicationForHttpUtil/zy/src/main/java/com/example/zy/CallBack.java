package com.example.zy;

/**
 * Created by ZengYu on 2017/5/3.
 */

import org.json.JSONObject;

/**
 * 回调
 */
public abstract class CallBack{
    /**
     * 成功时返回
     *
     * @param response 服务器返回字符串，需判空
     */
    public abstract void onSuccess(String response);

    /**
     * 失败时返回
     *
     * @param message 错误信息，需判空
     * @param e       异常，需判空
     */
    public abstract void onFailure(String message, Exception e);

    /**
     * 请求的信息
     *
     * @param url    请求地址
     * @param params 附加参数
     */
    public void onBefore(String url, JSONObject params) {

    }
}
