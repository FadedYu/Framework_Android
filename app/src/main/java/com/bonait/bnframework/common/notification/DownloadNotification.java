package com.bonait.bnframework.common.notification;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.support.v4.app.NotificationCompat;
import android.text.format.Formatter;

import com.bonait.bnframework.R;
import com.bonait.bnframework.MainApplication;
import com.lzy.okgo.model.Progress;

import java.util.Date;
import java.util.Locale;


/**
 * Created by LY on 2019/4/18.
 * 下载通知栏notification
 */
public class DownloadNotification {

    private Context context;
    private int notificationId;
    private String Tag = "downloading"; // 下载通知栏标识
    private String downloadFileName;    // 下载文件名称

    public DownloadNotification(Context context) {
        notificationId = (int) new Date().getTime();
        this.context = context;
    }

    /**
     * 显示notification下载进度
     * */
    public void showProgress(Progress progress) {
        downloadFileName = progress.fileName;
        String downloadLength = Formatter.formatFileSize(MainApplication.getContext(), progress.currentSize);
        String totalLength = Formatter.formatFileSize(MainApplication.getContext(), progress.totalSize);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new NotificationCompat.Builder(context, MainNotification.CHANNEL_DOWNLOADING_ID)
                .setSmallIcon(R.drawable.icon_user_pic)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_pic))
                .setContentTitle(progress.fileName)
                .setContentText("下载进度:" + String.format(Locale.CHINA, "%s / %s", downloadLength, totalLength))
                .setProgress(100, (int) (progress.fraction * 100.0), false)
                .build();

        if (notificationManager != null) {
            notificationManager.notify(Tag,notificationId,notification);
        }
    }

    /**
     * 下载完成，关闭进度条通知栏，显示成功通知栏
     * */
    public void downloadComplete() {
        int downloadCompleteId = (int) new Date().getTime();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context, MainNotification.CHANNEL_DOWNLOAD_COMPLETE_ID)
                .setSmallIcon(R.drawable.icon_user_pic)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_pic))
                .setContentTitle(downloadFileName)
                .setContentText("下载完成！")
                .setPriority(NotificationCompat.PRIORITY_MAX)
                .setAutoCancel(true)
                .build();
        if (notificationManager != null) {
            notificationManager.notify(downloadCompleteId,notification);
            notificationManager.cancel(Tag,notificationId);
        }
    }

    /**
     * 下载错误，关闭进度条通知栏，显示错误通知栏
     * */
    public void downloadError() {
        int downloadCompleteId = (int) new Date().getTime();
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Notification notification = new NotificationCompat.Builder(context, MainNotification.CHANNEL_DOWNLOAD_COMPLETE_ID)
                .setSmallIcon(R.drawable.icon_user_pic)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.drawable.icon_user_pic))
                .setContentTitle(downloadFileName)
                .setContentText("下载失败，请重新下载！")
                .setAutoCancel(true)
                .build();
        if (notificationManager != null) {
            notificationManager.notify(downloadCompleteId,notification);
            notificationManager.cancel(Tag,notificationId);
        }
    }

}
