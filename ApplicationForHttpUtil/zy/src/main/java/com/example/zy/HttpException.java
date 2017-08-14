package com.example.zy;

import android.util.Log;

/**
 * Created by Agent ZengYu on 2017/8/14.
 */

public class HttpException{
    /**
     * 空链接异常
     */
    static class NullUrlException extends RuntimeException {
         public NullUrlException(String s){
             super(s);
         }
    }

    /**
     * 空回调异常
     */
    static class NullCallBackException extends RuntimeException {
        public NullCallBackException(String s){
            super(s);
        }
    }

    /**
     * 响应码异常
     */
    static class ResponseCodeException extends RuntimeException {
        public ResponseCodeException(String s) {
            super(s);
        }
    }
}
