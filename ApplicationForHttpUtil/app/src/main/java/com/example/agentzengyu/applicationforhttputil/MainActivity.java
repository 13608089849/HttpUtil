package com.example.agentzengyu.applicationforhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.zy.CallBack;
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
        new HttpUtil().setUrl("url")
                .addParams("key1", "value1")
                .addParams("key2", "value2")
                .setConnectTimeOut(5000)
                .setReadTimeOut(5000)
                .execute(new CallBack() {
                    @Override
                    public void onSuccess(String s) {

                    }

                    @Override
                    public void onFailure(String s, Exception e) {

                    }

                    @Override
                    public void onBefore(String url, JSONObject params) {
                        super.onBefore(url, params);
                    }
                });
    }
}
