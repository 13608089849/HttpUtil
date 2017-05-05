package com.example.agentzengyu.applicationforhttputil;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Test();
    }

    private void Test() {
        HttpUtil httpUtil = new HttpUtil();
        httpUtil.setUrl("url")
                .setConnectTimeOut(5000)
                .execute(new CallBack() {
                    @Override
                    public void onSuccess(String string) {
                        Log.e("onSuccess", string);
                    }

                    @Override
                    public void onFailure(String string, Exception e) {
                        Log.e("String", string);
                        Log.e("Exception", e.getMessage());
                    }
                });
    }
}
