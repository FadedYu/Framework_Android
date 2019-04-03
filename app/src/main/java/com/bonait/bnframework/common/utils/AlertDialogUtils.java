package com.bonait.bnframework.common.utils;

import android.content.Context;

import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

/**
 * Created by LY on 2019/3/25.
 */
public class AlertDialogUtils {

    // 定义对话框主题样式
    public static final int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    /**
     * 对话框，只有确定按钮
     * */
    public static void showDialog(Context context,String title,String message) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .addAction("确定", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 对话框，有取消确定按钮
     * */
    public static void showDialog(Context context, String title, String message, QMUIDialogAction.ActionListener onClickListener) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("确定", onClickListener)
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 对话框，自定义确定按钮
     * */
    public static void showDialog(Context context, String title, String message,String ok, QMUIDialogAction.ActionListener onClickListener) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(ok, onClickListener)
                .create(mCurrentDialogStyle).show();
    }

    /**
     * 对话框，带红色按钮
     * */
    public static void showRedButtonDialog(Context context, String title, String message,String ok, QMUIDialogAction.ActionListener onClickListener) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(false)
                .setTitle(title)
                .setMessage(message)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction(0, ok, QMUIDialogAction.ACTION_PROP_NEGATIVE, onClickListener)
                .create(mCurrentDialogStyle).show();
    }

}
