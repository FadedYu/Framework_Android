package com.bonait.bnframework.system.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;

import com.bonait.bnframework.application.ActivityLifecycleManager;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.constant.SPConstants;
import com.bonait.bnframework.common.http.callback.json.JsonCallback;
import com.bonait.bnframework.common.model.BaseCodeJson;
import com.bonait.bnframework.common.utils.PreferenceUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.orhanobut.logger.Logger;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

public class WelcomeActivity extends AppCompatActivity {

    private Handler handler = new Handler();
    /*
    * 启动模式：
    * 1：启动界面时间与App加载时间相等
    * 2：设置启动界面2秒后跳转
    * */
    private final static int SELECT_MODE = 1;
    private OkHttpClient.Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_welcome);
        initWelcome();
    }

    private void initWelcome() {
        switch (WelcomeActivity.SELECT_MODE) {
            case 1:
                fastWelcome();
                break;
            case 2:
                slowWelcome();
                break;
        }
    }


    /*方法1：启动界面时间与App加载时间相等*/
    private void fastWelcome() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                //耗时任务，比如加载网络数据
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //判断token
                        doPost();
                    }
                });
            }
        }).start();
    }

    /*方法2：设置启动界面2秒后跳转*/
    private void slowWelcome() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                //判断token
                doPost();
            }
        }, 2000);
    }


    /**
     * 请求服务器判断token是否过期
     */
    private void doPost() {
        //判断是否用户修改过密码
        boolean isChangePwd = PreferenceUtils.getBoolean(SPConstants.CHANGE_PWD, false);
        if (isChangePwd) {
            //若修改过密码，则使token失效
            PreferenceUtils.setString(SPConstants.TOKEN, "");
        }
        String token = PreferenceUtils.getString(SPConstants.TOKEN, "");
        // 请求后台判断token
        String url = Constants.SERVICE_IP + "/checkToken.do";

        // 修改请求超时时间为6s，与全局超时时间分开
        builder = new OkHttpClient.Builder();
        builder.readTimeout(2000, TimeUnit.MILLISECONDS);
        builder.writeTimeout(2000, TimeUnit.MILLISECONDS);
        builder.connectTimeout(2000, TimeUnit.MILLISECONDS);

        OkGo.<BaseCodeJson<Void>>post(url)
                .tag(this)
                .client(builder.build())
                .params("token", token)
                .execute(new JsonCallback<BaseCodeJson<Void>>() {
                    @Override
                    public void onSuccess(Response<BaseCodeJson<Void>> response) {
                        BaseCodeJson baseCodeJson = response.body();
                        Logger.d(baseCodeJson.getMsg());
                        // token未过期，跳转到主界面
                        Intent intent = new Intent(WelcomeActivity.this, BottomNavigation2Activity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        // 结束所有Activity
                        ActivityLifecycleManager.get().finishAllActivity();
                    }

                    @Override
                    public void onError(Response<BaseCodeJson<Void>> response) {
                        super.onError(response);
                        // 跳转到登录页面
                        Intent intent = new Intent(WelcomeActivity.this, LoginActivity.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                        // 结束所有Activity
                        ActivityLifecycleManager.get().finishAllActivity();
                    }
                });
    }

    /**
     * 屏蔽物理返回键
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        OkGo.cancelAll(builder.build());

        if (handler != null) {
            //If token is null, all callbacks and messages will be removed.
            handler.removeCallbacksAndMessages(null);
        }
        super.onDestroy();
    }

}
