package com.example.agentzengyu.applicationforhttputil;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
                    public void Response(String string) {
                        Log.e("Response", string);
                    }
                });
    }
}
