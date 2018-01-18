package com.lhj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;

public class Setup4Activity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        //设置黑色状态栏字体
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_setup4);
    }
    public void nextPage(View v) {
        Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
        startActivity(intent);
        finish();
        SpUtils.putBoolean(this, ConstantValue.SETUP_OVER,true);
    }

    public void prePage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
    }

}
