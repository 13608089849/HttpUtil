package com.example.zy;

/**
 * Created by ZengYu on 2017/5/3.
 */

import org.json.JSONObject;

/**
 * 回调
 */
public interface CallBack {
    /**
     * 成功时返回
     *
     * @param string 需判空
     */
    void onSuccess(String string);

    /**
     * 失败时返回
     *
     * @param string 需判空
     * @param e      需判空
     */
    void onFailure(String string, Exception e);

    /**
     * 请求的信息
     *
     * @param url        请求地址
     * @param jsonObject 附加参数
     */
    void onBefore(String url, JSONObject jsonObject);
}
