package com.lhj.mobilesafe.activity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;
import com.lhj.mobilesafe.utils.ToastUtil;
import com.orhanobut.logger.Logger;

public class Setup3Activity extends Activity{

    private EditText et_phone_number;

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
        setContentView(R.layout.activity_setup3);
        initUI();
//        Logger.i("进来了");
    }

    private void initUI() {
        //显示电话号码
        et_phone_number = findViewById(R.id.et_phone_number);
        //获取联系人电话的回显
        String phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        et_phone_number.setText(phone);
        //点击选择联系人
        Button bt_select_number = findViewById(R.id.bt_select_number);
        bt_select_number.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Logger.i("点击了");
                Intent intent = new Intent(getApplicationContext(), ContacListActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (data!=null){

            //返回到当前界面的时候要去接受结果的方法
            String phone = data.getStringExtra("phone");
            //将特殊字符过滤
            phone = phone.replace("-", "").replace(" ","").trim();
            et_phone_number.setText(phone);
            //存储联系人
//            SpUtils.putString(getApplication(), ConstantValue.CONTACT_PHONE,phone);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void nextPage(View v) {
        String phone = et_phone_number.getText().toString();
        if (!TextUtils.isEmpty(phone)){
            SpUtils.putString(getApplication(), ConstantValue.CONTACT_PHONE,phone);
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();
            //下一页动画
            overridePendingTransition(R.anim.next_in_anim,R.anim.next_out_anim);
        }else{
            ToastUtil.show(getApplicationContext(),"请输入电话号码");
        }

        //sp存储了相关联系人以后可以跳转到下一页
/*
        String contact_phone = SpUtils.getString(getApplicationContext(), ConstantValue.CONTACT_PHONE, "");
        if (!TextUtils.isEmpty(contact_phone)){
            Intent intent = new Intent(getApplicationContext(), Setup4Activity.class);
            startActivity(intent);
            finish();
        }else{
            ToastUtil.show(getApplicationContext(),"请选项联系人");
        }
*/
    }

    public void prePage(View v) {
        Intent intent = new Intent(getApplicationContext(), Setup2Activity.class);
        startActivity(intent);
        finish();
        //上一页动画
        overridePendingTransition(R.anim.pre_in_anim,R.anim.pre_out_anim);
    }

}
