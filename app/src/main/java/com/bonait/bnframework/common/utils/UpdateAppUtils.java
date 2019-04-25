package com.bonait.bnframework.common.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.http.callback.files.FileProgressDialogCallBack;
import com.bonait.bnframework.common.http.callback.json.JsonDialogCallback;
import com.bonait.bnframework.modules.mine.model.UpdateAppPo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import java.io.File;

/**
 * Created by LY on 2019/4/1.
 */
public class UpdateAppUtils {

    /**
     * apk下载地址
     */
    private static String downloadUrl = "";

    /**
     * 获取服务器apk下载地址ID
     */
    private static String serviceApkId = "";
    /**
     * 当前版本号
     */
    private static int myVersionCode = 0;
    /**
     * 服务器的版本号码
     */
    private static int serviceVersionCode = 0;
    /**
     * 服务器的版本号名称
     */
    private static String serviceVersionName = "";
    /**
     * 更新说明
     */
    private static String description = "";

    /**
     * 更新APP版本入口
     */
    public static void updateApp(Context context) {
        //获取当前app版本号
        myVersionCode = AppUtils.getVersionCode(context);
        //获取json转成Gson，并获取版本等信息
        doPost(context);
    }

    /**
     * 请求后台服务器，检查apk版本
     */
    private static void doPost(final Context context) {
        String getNewVersionUrl = Constants.SERVICE_IP + "/iandroid/appVersionAction!getNewVersion.do";
        OkGo.<UpdateAppPo>post(getNewVersionUrl)
                .tag(context)
                .execute(new JsonDialogCallback<UpdateAppPo>(context) {
                    @Override
                    public void onSuccess(Response<UpdateAppPo> response) {
                        UpdateAppPo updateAppPo = response.body();
                        if (updateAppPo != null) {
                            serviceVersionCode = updateAppPo.getVersion();
                            description = updateAppPo.getDescription();
                            serviceApkId = updateAppPo.getApkId();
                            //获取apk下载地址
                            String url = Constants.SERVICE_IP + "/file-download?fileId=";
                            downloadUrl = url + serviceApkId;
                            // 判断Apk是否是最新版本
                            if (myVersionCode < serviceVersionCode) {
                                showUpdateDialog(context);
                            } else {
                                ToastUtils.info("当前版本已是最新版本");
                            }

                        }
                    }
                });
    }

    /**
     * 弹出下载对话框，里面有一些更新版本的信息
     */
    private static void showUpdateDialog(final Context context) {
        new QMUIDialog.MessageDialogBuilder(context)
                .setCancelable(false)
                .setTitle("发现新版本")
                .setMessage(description)
                .addAction("取消", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        dialog.dismiss();
                    }
                })
                .addAction("去更新", new QMUIDialogAction.ActionListener() {
                    @Override
                    public void onClick(QMUIDialog dialog, int index) {
                        downloadApk(context);
                        dialog.dismiss();
                    }
                })
                .create(AlertDialogUtils.mCurrentDialogStyle)
                .show();
    }

    private static void downloadApk(final Context context) {
        OkGo.<File>get(downloadUrl)
                .tag(context)
                .execute(new FileProgressDialogCallBack(context) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        File file = response.body();
                        String absolutePath = file.getAbsolutePath();
                        installApk(context, absolutePath);
                    }
                });
    }

    /**
     * 跳转apk安装界面
     */
    public static void installApk(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if (file.length() > 0 && file.exists() && file.isFile()) {
            //判断是否是AndroidN以及更高的版本
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                i.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
                Uri contentUri = FileProvider.getUriForFile(context, context.getPackageName() + ".fileProvider", file);
                i.setDataAndType(contentUri, "application/vnd.android.package-archive");
            } else {
                i.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
                i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            }
            context.startActivity(i);
        }
    }
}
