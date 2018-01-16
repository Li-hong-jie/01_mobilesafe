package com.lhj.mobilesafe.activity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.lhj.mobilesafe.R;
import com.lhj.mobilesafe.utils.ConstantValue;
import com.lhj.mobilesafe.utils.SpUtils;
import com.lhj.mobilesafe.utils.StreamUtil;
import com.lhj.mobilesafe.utils.ToastUtil;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.orhanobut.logger.AndroidLogAdapter;
import com.orhanobut.logger.FormatStrategy;
import com.orhanobut.logger.Logger;
import com.orhanobut.logger.PrettyFormatStrategy;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    /**
     * 更新新版本的状态码
     */
    public static final int UPDATE_VERSION = 100;
    /**
     * 进入程序主界面的状态码
     */
    public static final int ENTER_HOME = 101;
    public static final int URL_ERROR = 102;
    public static final int IO_ERROR = 103;
    public static final int JSON_ERROR = 104;
    private TextView tv_version;
    private RelativeLayout rl_root;
    private int mVersionCode;
    private String mVersionDes;
    private String mDownloadUrl;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_VERSION:
                    //弹出对话框提示用户更新
                    showUpdateDialog();
                    break;
                case ENTER_HOME:
                    //进入程序
                    enterHome();
                    break;
                case URL_ERROR:
                    ToastUtil.show(SplashActivity.this, "url异常");
                    enterHome();
                    break;
                case IO_ERROR:
                    ToastUtil.show(SplashActivity.this, "io读取异常");
                    enterHome();
                    break;
                case JSON_ERROR:
                    ToastUtil.show(SplashActivity.this, "json解析异常");
                    enterHome();
                    break;
            }
        }
    };

    /**
     * 弹出对话框,提示更新
     */
    private void showUpdateDialog() {
        //对话框是依赖于activity存在的
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setIcon(R.mipmap.ic_launcher_round);
        builder.setTitle("版本更新");
        builder.setMessage(mVersionDes);
        builder.setPositiveButton("立即更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //下载apk,apk链接downloadUrl
                Logger.i("应用下载");
                downloadApk();
            }
        });
        builder.setNegativeButton("稍后更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                enterHome();
            }
        });
        //点击取消的事件
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                //即使用户点击取消也进入主界面
                enterHome();
                dialogInterface.dismiss();
            }
        });
        builder.show();
    }

    private void downloadApk() {
        //apk下载链接地址,放置apk的所在路径

        //1.判断SD卡是否可用
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            //2.获取SD卡路径
            String path = Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "mebilesafe.apk";
            //3.发送请求获取apk.放置到指定路径
            HttpUtils httpUtils = new HttpUtils();
            //4.发送请求,传递参数(下载地址,存储位置)
            httpUtils.download(mDownloadUrl, path, new RequestCallBack<File>() {
                @Override
                public void onSuccess(ResponseInfo<File> responseInfo) {
                    //下载成功(下载后放在SD卡的apk)
                    Logger.i("下载成功");
                    File file = responseInfo.result;
                    //提示用户安装
                    installApk(file);
                }

                @Override
                public void onFailure(HttpException e, String s) {
                    Logger.i("下载失败");
                    //下载失败
                }

                //刚刚开始下载的方法
                @Override
                public void onStart() {
                    Logger.i("刚刚开始下载");
                    super.onStart();
                }

                //下载中的方法(下载内容总大小,当前下载位置,是否正在下载)
                @Override
                public void onLoading(long total, long current, boolean isUploading) {
                    Logger.i("下载中......");
                    super.onLoading(total, current, isUploading);
                }
            });
        }
    }

    /**
     * 安装对应的apk
     *
     * @param file 安装的文件
     */
    private void installApk(File file) {
        //系统应用安装界面.源码,安装apk的入口
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
/*
        //文件作为数据源
        intent.setData(Uri.fromFile(file));
        //设置安装的类型
        intent.setType("application/vnd.android.package-archive");
*/
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
//        startActivity(intent);
        startActivityForResult(intent, 0);
    }

    //开启一个activity后,返回结果调用的方法
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        enterHome();
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 进入应用程序主界面
     */
    private void enterHome() {
        Intent intent = new Intent(this, HomeActivity.class);
        startActivity(intent);
        //在开启一个新的界面之后将导航界面关闭
        finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_splash);
        //初始化Logger
        myLogger();
        //初始化UI
        initUI();
        //初始化数据
        initDate();
        //初始化动画
        initAnimation();
    }

    /**
     * 添加淡入的动画效果
     */
    private void initAnimation() {
        AlphaAnimation alphaAnimation = new AlphaAnimation(0, 1);
        alphaAnimation.setDuration(500);
        rl_root.startAnimation(alphaAnimation);
    }

    private void myLogger() {
        FormatStrategy formatStrategy = PrettyFormatStrategy.newBuilder()
                .methodCount(1)         //（可选）要显示多少个方法行。默认2
                .tag("MY_LOGGER")   //（可选）每个日志的全局标记。默认PRETTY_LOGGER
                .build();
        Logger.addLogAdapter(new AndroidLogAdapter(formatStrategy));
    }

    /**
     * 获取数据方法
     */
    private void initDate() {
        //获取本地版本号
        mVersionCode = getVersionCode();
        //1.应用版本名称
        String version_text = "版本名称:" + getVersionName() + "（" + mVersionCode + "）";
        tv_version.setText(version_text);
        //2.检测(本地版本号和服务器版本号对比)是否有更新,如果有更新,提示用户下载(member)
        Logger.i("当前版本号:%s", mVersionCode);
        //3.获取服务器版本号(客户端发请求,服务端给响应,(josn,xml))
        //josn返回的数据(版本名称,版本描述,服务器版本号,下载地址)
        if (SpUtils.getBoolean(this, ConstantValue.OPEN_UPDATE,false)) {
            checkVersion();
        }else{
//            enterHome();
            //发送消息2秒后去处理消息
            mHandler.sendEmptyMessageDelayed(ENTER_HOME,2000);
        }
    }

    private void checkVersion() {
        new Thread() {
            public void run() {
                //发送请求获取数据,参数则为请求json的链接地址
                Message msg = Message.obtain();
                long startTime = System.currentTimeMillis();
                try {
                    //1.封装url地址
                    URL url = new URL("http://192.168.0.100:8080/update.json");
                    //2.开启一个链接
                    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                    //3.设置常见的亲求参数(请求头)
                    //亲求超时
                    connection.setConnectTimeout(2000);
                    //读取超时
                    connection.setReadTimeout(5000);
                    //默认GET方式亲求
                    connection.setRequestMethod("GET");
                    //4.获取响应码
                    if (connection.getResponseCode() == 200) {
                        //5.以流的形式,将数据获取下来
                        InputStream is = connection.getInputStream();
                        //6.将流转换为字符串
                        String json = StreamUtil.streamToString(is);
                        Logger.json(json);
                        //7.json的解析
                        JSONObject jsonObject = new JSONObject(json);
                        String versionName = jsonObject.getString("versionName");
                        mVersionDes = jsonObject.getString("versionDes");
                        String versionCode = jsonObject.getString("versionCode");
                        mDownloadUrl = jsonObject.getString("downloadUrl");
                        Logger.i("versionName=%s", versionName);
                        Logger.i("versionDes=%s", mVersionDes);
                        Logger.i("versionCode=%s", versionCode);
                        Logger.i("downloadUrl=%s", mDownloadUrl);
                        //8.比对版本号
                        if (mVersionCode < Integer.parseInt(versionCode)) {
                            //提示用户有新版更新(弹出对话框)
                            msg.what = UPDATE_VERSION;
                        } else {
                            //进入程序主界面
                            msg.what = ENTER_HOME;
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    msg.what = URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    msg.what = IO_ERROR;
                } catch (JSONException e) {
                    e.printStackTrace();
                    msg.what = JSON_ERROR;
                } finally {
                    //指定睡眠时间,请求网络时间超过4秒则不做撤离
                    //请求网络时间不足4秒,强制将其睡眠满4秒
                    long endTime = System.currentTimeMillis();
                    if (endTime - startTime < 4000) {
                        try {
                            Thread.sleep(4000 - (endTime - startTime));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    mHandler.sendMessage(msg);
                }
            }
        }.start();
/*
        new Thread(new Runnable() {
            @Override
            public void run() {

            }
        });
*/
    }

    /**
     * 返回版本号
     *
     * @return 非0, 则为成功
     */
    private int getVersionCode() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中,获取指定包名的基本信息(版本名称,版本号)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3.获取对应的版本
            return packageInfo.versionCode;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取版本名称:清单文件中
     *
     * @return 返回版本名称 返回null带便有异常
     */
    @Nullable
    private String getVersionName() {
        //1.包管理者对象packageManager
        PackageManager pm = getPackageManager();
        //2.从包管理者对象中,获取指定包名的基本信息(版本名称,版本号)
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            //3.获取对应的版本
            return packageInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 初始化UI的方法
     */
    private void initUI() {
        tv_version = findViewById(R.id.tv_version);
        rl_root = findViewById(R.id.rl_root);
    }

}
