package com.bonait.bnframework.common.http.callback.bitmap;

import android.content.Context;
import android.graphics.Bitmap;

import com.lzy.okgo.callback.BitmapCallback;
import com.lzy.okgo.request.base.Request;
import com.qmuiteam.qmui.widget.dialog.QMUITipDialog;

/**
 * Created by LY on 2019/4/2.
 *
 * 如果请求的数据是图片，则可以使用该回调，回调的图片进行了压缩处理，确保不发生OOM
 *
 * 有加载框的网络图片请求回调
 *
 */
public abstract class BitmapDialogCallback extends BitmapCallback {

    private QMUITipDialog tipDialog;


    public BitmapDialogCallback(Context context) {
        super(1000,1000);
        tipDialog = new QMUITipDialog.Builder(context)
                .setIconType(QMUITipDialog.Builder.ICON_TYPE_LOADING)
                .setTipWord("正在加载")
                .create();
    }

    @Override
    public void onStart(Request<Bitmap, ? extends Request> request) {
        if (tipDialog != null && !tipDialog.isShowing()) {
            tipDialog.show();
        }
    }

    @Override
    public void onFinish() {
        if (tipDialog != null && tipDialog.isShowing()) {
            tipDialog.dismiss();
        }
    }
}
