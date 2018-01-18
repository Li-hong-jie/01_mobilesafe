package com.lhj.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.telecom.PhoneAccountHandle;
import android.telecom.TelecomManager;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;
import com.lhj.mobilesafe.view.SettingItemView;

public class Setup2Activity extends Activity {

    private SettingItemView siv_sim_bound;

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
        setContentView(R.layout.activity_setup2);
        initUI();
    }

    private void initUI() {
        siv_sim_bound = findViewById(R.id.siv_sim_bound);
        //回显(读取已有的状态,用作显示,sp中是否存储了SIM卡的序列号)
        String sim_number = SpUtils.getString(this, ConstantValue.SIM_NUMBER, "");
        //判断序列号是否为空
        if (TextUtils.isEmpty(sim_number)){
            siv_sim_bound.setCheck(false);
        }else{
            siv_sim_bound.setCheck(true);
        }

        siv_sim_bound.setOnClickListener(new View.OnClickListener() {
            @TargetApi(Build.VERSION_CODES.M)
            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onClick(View view) {
                //获取原有的状态(isChecked)
                boolean isCheck = siv_sim_bound.isCheck();
                //将状态拿出来,取反,状态设置给当前条目,存储卡序列号
                if (!isCheck){
                    //存储卡序列号
                    //获取SIM卡序列号
                    @SuppressLint("ServiceCast") TelecomManager manager = (TelecomManager) getSystemService(Context.TELEPHONY_SERVICE);
//                    String simSerialNumber=manager.getSimSerialNumber();
//                    PhoneAccountHandle simCallManager = manager.getSimCallManager();
//                    String simSerialNumber=android.os.SystemProperties.get(android.telephony.TelephonyProperties.PROPERTY_IMSI);
//                    SpUtils.putString(getApplicationContext(),ConstantValue.SIM_NUMBER,simSerialNumber);
                }else{
                    //将存储卡序列号的节点从sp中删除
                    SpUtils.remove(getApplicationContext(),ConstantValue.SIM_NUMBER);
                }
            }
        });
    }

    public void nextPage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup3Activity.class);
        startActivity(intent);
        finish();
    }

    public void prePage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup1Activity.class);
        startActivity(intent);
        finish();
    }


}
