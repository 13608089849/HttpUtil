package com.example.agentzengyu.applicationforhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.zy.HttpCallback;
import com.example.zy.HttpUtil;

import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Test();
    }

    private void Test() {
        HttpUtil.setUrl("https://github.com/13608089849/HttpUtil/raw/master/HttpUtil_V1.8.jar")
                .setConnectTimeOut(5000)
                .setReadTimeOut(5000)
                .execute(new HttpCallback() {
                    @Override
                    public void onSuccess(String s) {
                        Log.e("onSuccess",s);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onBefore(String url, JSONObject params) {
                        super.onBefore(url, params);
                    }
                });
    }
}
