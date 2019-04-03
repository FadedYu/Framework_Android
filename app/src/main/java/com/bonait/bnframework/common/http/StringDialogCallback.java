package com.bonait.bnframework.common.http;

import android.content.Context;

import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by LY on 2019/4/2.
 *
 * 对convertResponse()按文本解析，解析的编码依据服务端响应头中的Content-Type中的编码格式，自动进行编码转换，确保不出现乱码
 *
 * 有加载框的文本解析回调
 *
 */
public abstract class StringDialogCallback extends StringCallback {

    private QMUITipDialog tipDialog;

    /**
     * 展示加载框
     * */
    public StringDialogCallback(Context context) {
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }


    /**
     * 请求网络开始前弹出加载框
     * */
    @Override
    public void onStart(Request<String, ? extends Request> request) {
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
