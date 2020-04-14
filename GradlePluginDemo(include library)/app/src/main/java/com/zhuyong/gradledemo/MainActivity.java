package com.zhuyong.gradledemo;

import android.os.Bundle;

import com.zhuyong.mylibrary.ToastUtils;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        showToast();
    }

    /**
     * 弹出一个Toast
     */
    public void showToast() {
        ToastUtils.showToasShort(this);
    }
}
