package com.bonait.bnframework.common.http;

import android.content.Context;

import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by LY on 2019/4/1.
 *
 * 在网络请求前显示一个loading，请求结束后取消loading
 *
 * 有加载框的网络请求回调
 *
 */
public abstract class DialogCallback<T> extends JsonCallback<T> {

    private QMUITipDialog tipDialog;

    /**
     * 弹出加载框入口
     * */
    public DialogCallback(Context context) {
        super();
        initDialog(context);
    }

    /**
     * 初始化加载框
     * */
    private void initDialog(Context context) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }

    /**
     * 请求网络开始前弹出加载框
     * */
    @Override
    public void onStart(Request<T, ? extends Request> request) {
        if (tipDialog != null && !tipDialog.isShowing()) {
            tipDialog.show();
        }
    }

    /**
     * 请求网络结束后关闭加载框
     * */
    @Override
    public void onFinish() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }
}
