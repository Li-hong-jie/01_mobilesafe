package com.lhj.mobilesafe.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;
import com.lhj.mobilesafe.utils.ToastUtil;

public class Setup4Activity extends Activity {

    private CheckBox cb_box;

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
        initUI();
        initData();
    }

    private void initData() {

    }

    private void initUI() {
        cb_box = findViewById(R.id.cb_box);
        //是否选中状态的回显-----------您还没有开启防盗保护
        boolean open_security = SpUtils.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        cb_box.setChecked(open_security);
        if (open_security) {
            cb_box.setText("防盗保护已开启");
        } else {
            cb_box.setText("防盗保护未开启");
        }
        //点击过程中,选择框状态切换并存储
        cb_box.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
//                cb_box.setChecked(!cb_box.isChecked());
                SpUtils.putBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, b);
                //根据状态修改显示的内容
                if (b) {
                    cb_box.setText("防盗保护已开启");
                } else {
                    cb_box.setText("防盗保护未开启");
                }
            }
        });
    }

    public void nextPage(View v) {
        boolean open_security = SpUtils.getBoolean(getApplicationContext(), ConstantValue.OPEN_SECURITY, false);
        if (open_security) {
            Intent intent = new Intent(getApplicationContext(), SetupOverActivity.class);
            startActivity(intent);
            finish();
            //下一页动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
            SpUtils.putBoolean(this, ConstantValue.SETUP_OVER, true);
        } else {
            ToastUtil.show(getApplicationContext(),"请开启防盗保护");
        }

    }

    public void prePage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
        //上一页动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

}
