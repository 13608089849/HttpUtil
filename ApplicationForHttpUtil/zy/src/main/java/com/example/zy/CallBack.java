package com.example.zy;

/**
 * Created by ZengYu on 2017/5/3.
 */

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
}
