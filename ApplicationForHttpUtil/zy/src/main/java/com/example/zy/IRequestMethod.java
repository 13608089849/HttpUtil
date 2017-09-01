package com.example.zy;

import android.support.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Created by Agent ZengYu on 2017/9/1.
 */

/**
 * 请求方法
 */
@StringDef({IRequestMethod.POST, IRequestMethod.GET})
@Retention(RetentionPolicy.SOURCE)
public @interface IRequestMethod {
    String POST = "POST";
    String GET = "GET";
}
