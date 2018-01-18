package com.lhj.mobilesafe.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

public class SpUtils {

    private static SharedPreferences sp;

    /**
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值 boolean
     */
    public static void putBoolean(Context ctx, String key, boolean value) {
        //存储节点文件名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putBoolean(key, value).commit();
    }

    /**
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 默认存储节点的值 boolean
     * @return 默认值或者此节点读取到的方法
     */
    @NonNull
    public static boolean getBoolean(Context ctx, String key, boolean defValue) {
        //存储节点文件名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getBoolean(key, defValue);
    }

    /**
     * @param ctx   上下文环境
     * @param key   存储节点名称
     * @param value 存储节点的值 String
     */
    public static void putString(Context ctx, String key, String value) {
        //存储节点文件名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().putString(key, value).commit();
    }

    /**
     * @param ctx      上下文环境
     * @param key      存储节点名称
     * @param defValue 默认存储节点的值
     * @return 默认值或者此节点读取到的方法
     */
    @NonNull
    public static String getString(Context ctx, String key, String defValue) {
        //存储节点文件名称,读写方式
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        return sp.getString(key, defValue);
    }

    /**
     * 从sp中移除指定节点
     *
     * @param ctx 上下文环境
     * @param key 需要移除节点的名称
     */
    @SuppressLint("CommitPrefEdits")
    public static void remove(Context ctx, String key) {
        if (sp == null) {
            sp = ctx.getSharedPreferences("config", Context.MODE_PRIVATE);
        }
        sp.edit().remove(key);
    }
}
