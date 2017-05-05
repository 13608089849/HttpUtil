package com.example.agentzengyu.applicationforhttputil;

/**
 * Created by ZengYu on 2017/5/3.
 */

public interface CallBack {
    void onSuccess(String string);

    void onFailure(String string, Exception e);
}
