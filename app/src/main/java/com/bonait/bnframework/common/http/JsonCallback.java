package com.bonait.bnframework.common.http;

import android.app.Activity;
import android.content.Intent;

import com.bonait.bnframework.application.ActivityLifecycleManager;
import com.bonait.bnframework.common.utils.PreferenceUtils;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.lzy.okgo.callback.AbsCallback;
import com.lzy.okgo.exception.HttpException;
import com.lzy.okgo.exception.StorageException;
import com.lzy.okgo.model.Response;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

/**
 * Created by LY on 2019/4/2.
 */
public abstract class JsonCallback<T> extends AbsCallback<T> {

    private Type type;
    private Class<T> clazz;

    public JsonCallback() {
    }

    public JsonCallback(Type type) {
        this.type = type;
    }

    public JsonCallback(Class<T> clazz) {
        this.clazz = clazz;
    }

    /**
     * 该方法是子线程处理，不能做ui相关的工作
     * 主要作用是解析网络返回的 response 对象,生产onSuccess回调中需要的数据对象
     * 这里的解析工作不同的业务逻辑基本都不一样,所以需要自己实现,实际使用根据需要修改
     */
    @Override
    public T convertResponse(okhttp3.Response response) throws Throwable {

        // 不同的业务，这里的代码逻辑都不一样，如果你不修改，那么基本不可用

        //详细自定义的原理和文档，看这里： https://github.com/jeasonlzy/okhttp-OkGo/wiki/JsonCallback

        if (type == null) {
            if (clazz == null) {
                Type genType = getClass().getGenericSuperclass();
                type = ((ParameterizedType) genType).getActualTypeArguments()[0];
            } else {
                JsonConvert<T> convert = new JsonConvert<>(clazz);
                return convert.convertResponse(response);
            }
        }

        JsonConvert<T> convert = new JsonConvert<>(type);
        return convert.convertResponse(response);
    }


    @Override
    public void onError(Response<T> response) {
        super.onError(response);

        Throwable exception = response.getException();
        if (exception != null) {
            exception.printStackTrace();
        }

        if (exception instanceof UnknownHostException || exception instanceof ConnectException) {
            ToastUtils.error("网络连接失败，请连接网络！");
        } else if (exception instanceof SocketTimeoutException) {
            ToastUtils.error("网络请求超时，请稍后重试！");
        } else if (exception instanceof HttpException) {
            ToastUtils.error("暂时无法连接服务器，请稍后重试！");
        } else if (exception instanceof StorageException) {
            ToastUtils.error("SD卡不存在或者没有获取SD卡访问权限！");
        } else if (exception instanceof TokenException) {
            // 删除token，跳转到登录页面
            skipLoginActivityAndFinish(exception);
        } else if (exception instanceof IllegalStateException) {
            ToastUtils.error(exception.getMessage());
        }
    }

    /**
     * 删除token，跳转到登录页面
     * */
    private void skipLoginActivityAndFinish(Throwable exception) {
        //删除本地过期的token
        PreferenceUtils.remove("token");
        PreferenceUtils.remove("userId");
        Intent intent = new Intent("com.bonait.bnframework.system.activity.LoginActivity.ACTION_START");
        // 获取当前Activity（栈中最后一个压入的）
        Activity activity = ActivityLifecycleManager.get().currentActivity();
        // 结束所有Activity
        ActivityLifecycleManager.get().finishAllActivity();
        // 跳转到登录页面
        activity.startActivity(intent);
        activity.overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        //ToastUtils.error(exception.getMessage());
    }
}
