package com.bonait.bnframework.common.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

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
     * */
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
     * */
    public static void updateApp(Activity activity){
        //获取当前app版本号
        myVersionCode = AppUtils.getVersionCode(activity);
        //获取json转成Gson，并获取版本等信息
    }


    /**
     * 跳转apk安装界面
     * */
    public static void installApk(Context context, String filePath) {
        Intent i = new Intent(Intent.ACTION_VIEW);
        File file = new File(filePath);
        if ( file.length() > 0 && file.exists() && file.isFile()) {
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
