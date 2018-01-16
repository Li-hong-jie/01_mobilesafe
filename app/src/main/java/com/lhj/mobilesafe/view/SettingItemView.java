package com.lhj.mobilesafe.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhj.mobilesafe.R;
import com.orhanobut.logger.Logger;


public class SettingItemView extends RelativeLayout {

    public static final String NAMESPACE = "http://schemas.android.com/apk/res/com.lhj.mobilesafe";
    private CheckBox cb_box;
    private TextView tv_des;
    private String mDestitle;
    private String mDesoff;
    private String mDeson;

    public SettingItemView(Context context) {
        this(context, null);
    }

    public SettingItemView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SettingItemView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        //xml--->view 将设置界面的一个条目转换为view对象,直接添加到了当前的SettingItemView对应的view中
        View.inflate(context, R.layout.setting_item_view, this);
/*
        View view = View.inflate(context, R.layout.setting_item_view, null);
        this.addView(view);
*/
        TextView tv_title = findViewById(R.id.tv_title);
        tv_des = findViewById(R.id.tv_des);
        cb_box = findViewById(R.id.cb_box);

        //获取自定义以及原生属性的操作写在此处
        initAttrs(attrs);
        tv_title.setText(mDestitle);

    }

    /**
     * @param attrs 构造方法中维护好的属性集合
     *              返回属性集合中自定义属性属性值
     */
    private void initAttrs(AttributeSet attrs) {
        Logger.i("属性个数 = %s", attrs.getAttributeCount());
        //获取属性名称以及属性值
/*
        for (int i=0;i<attrs.getAttributeCount();i++){
            attrs.getAttributeName(i);
            Logger.i("name = %s,value = %s",attrs.getAttributeName(i),attrs.getAttributeValue(i));
        }
*/
        mDestitle = attrs.getAttributeValue(NAMESPACE, "destitle");
        mDesoff = attrs.getAttributeValue(NAMESPACE, "desoff");
        mDeson = attrs.getAttributeValue(NAMESPACE, "deson");

    }

    /**
     * 判断是否开启的方法
     *
     * @return 返回SettingItemView的选中状态
     */
    public boolean isCheck() {
        //由checkBox的选中状态判断
        return cb_box.isChecked();
    }

    /**
     * @param isCheck 是否作为开启的变量,由点击过程中传递
     */
    public void setCheck(boolean isCheck) {
        //当前条目在选择过程中,cb_box的状态随ischeck也变化
        cb_box.setChecked(isCheck);
        if (isCheck) {
            tv_des.setText(mDeson);
        } else {
            tv_des.setText(mDesoff);
        }
    }
}
