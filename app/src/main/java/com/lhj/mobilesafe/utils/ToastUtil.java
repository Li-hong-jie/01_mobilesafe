package com.lhj.mobilesafe.utils;

import android.content.Context;
import android.widget.Toast;

public class ToastUtil {
    /**
     * @param ctx 上下文环境
     * @param msg 打印的文本内容
     */
    //打印土司
    public static void show(Context ctx, String msg) {
        Toast.makeText(ctx, msg, Toast.LENGTH_SHORT).show();
    }
}
