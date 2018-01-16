package com.lhj.mobilesafe.activity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.WindowManager;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;
import com.lhj.mobilesafe.view.SettingItemView;

public class SettingActivity extends Activity{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }

        setContentView(R.layout.activity_setting);
        initUpdate();
    }

    /**
     * 版本更新开关
     */
    private void initUpdate() {
        final SettingItemView siv_update = findViewById(R.id.siv_update);
        //获取已有的开关状态,用作显示
        boolean open_update = SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE, false);
        //是否选中根据上一次存储的结果显示
        siv_update.setCheck(open_update);
        siv_update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //如果之前是选中状态,点击之后变为未选中...
                boolean isCheck = siv_update.isCheck();
                //将原有状态取反,等同于上述两步操作
                siv_update.setCheck(!isCheck);
                //将去取反后的存储状态存到相应的sp中
                SpUtils.putBoolean(getApplicationContext(),ConstantValue.OPEN_UPDATE,!isCheck);
            }
        });
    }

}
