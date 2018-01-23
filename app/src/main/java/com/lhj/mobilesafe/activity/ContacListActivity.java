package com.lhj.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.lhj.mobilesafe.R;
import com.orhanobut.logger.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ContacListActivity extends Activity {

    private ListView lv_contact;
    private List<HashMap<String, String>> contactList = new ArrayList<>();
    private MyAdapter mAdapter;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            //更新ui,填充数据适配器
            mAdapter = new MyAdapter();
            lv_contact.setAdapter(mAdapter);
        }
    };

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
        setContentView(R.layout.activity_contact_list);
        initUI();
        initData();
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return contactList.size();
        }

        @Override
        public HashMap<String, String> getItem(int i) {
            return contactList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View v, ViewGroup viewGroup) {
            @SuppressLint("ViewHolder") View view = View.inflate(getApplicationContext(), R.layout.listview_contact_item, null);
            TextView tv_name = findViewById(R.id.tv_name);
            TextView tv_phone = findViewById(R.id.tv_phone);

            tv_name.setText(getItem(i).get("name"));
            tv_phone.setText(getItem(i).get("phone"));
            return view;
        }
    }

    /**
     * 获取系统联系人的数据的方法
     */
    private void initData() {
        Logger.i("开始获取联系人");
        new Thread() {
            @Override
            public void run() {
                //获取内容解析者
                ContentResolver contentResolver = getContentResolver();
                //查询(权限)
                Cursor cursor = contentResolver.query(Uri.parse("content://com.android.contacts/raw_contacts"),
                        new String[]{"contact_id"}, null, null, null);
                contactList.clear();
                //循环游标,知道没有数据为止
                while (cursor.moveToNext()) {
                    String id = cursor.getString(0);
                    Logger.i("id=%s", id);
                    //根据用户唯一性id去查data表和mimetype表生成的视图,其查询相关字段
                    Cursor indexCursor = contentResolver.query(Uri.parse("content://com.android.contacts/data"),
                            new String[]{"data1", "mimetype"}, "raw_contact_id=?", new String[]{id}, null);
                    //循环获取每一个联系人的电话\姓名,数据类型
                    HashMap<String, String> hashMap = new HashMap<>();
                    while (indexCursor.moveToNext()) {
                        String data = indexCursor.getString(0);
                        String type = indexCursor.getString(1);
                        //区分类型去给 hashMap 填充数据
                        if (type.equals("vnd.android.cursor.item/phone_v2")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("phone", data);
                            }
                        } else if (type.equals("vnd.android.cursor.item/name")) {
                            if (!TextUtils.isEmpty(data)) {
                                hashMap.put("name", data);
                            }
                        }
                    }
                    indexCursor.close();
                    contactList.add(hashMap);
                }
                cursor.close();
                //消息机制,发送一个空的消息
                mHandler.sendEmptyMessage(0);
            }
        }.start();
    }

    private void initUI() {
        lv_contact = findViewById(R.id.lv_contact);
        lv_contact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                //1,获取点中条目的索引指向集合中的对象
                if (mAdapter != null) {
                    HashMap<String, String> hashMap = mAdapter.getItem(i);
                    //2,获取当前条目指向集合对应的电话号码
                    String phone = hashMap.get("phone");
                    //3,电话号码用于第三个界面

                    //4在结束此界面返回导航3,将数据返回
                    Intent intent = new Intent();
                    intent.putExtra("phone", phone);
                    setResult(0, intent);
                    finish();
                }
            }
        });
    }
}
