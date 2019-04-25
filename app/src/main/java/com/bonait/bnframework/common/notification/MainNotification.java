package com.bonait.bnframework.common.notification;

import android.app.NotificationChannel;
import android.app.NotificationChannelGroup;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;


/**
 * Created by LY on 2019/4/22.
 * 初始化notification，针对Android8.0以上创建消息渠道组和消息渠道
 */
public class MainNotification {

    public static final String GROUP_DOWNLOAD_ID = "group_download_id";
    public static final String GROUP_PUSH_MESSAGE_ID = "group_push_message_id";

    public static final String CHANNEL_DOWNLOADING_ID = "channel_downloading_id";
    public static final String CHANNEL_DOWNLOAD_COMPLETE_ID = "channel_download_complete_id";
    public static final String CHANNEL_MESSAGE_ID = "channel_message_id";

    /**
     * 初始化Notification 消息通知渠道
     */
    public static void initNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // 创建渠道组
            createNotificationGroup(context, GROUP_DOWNLOAD_ID, "下载消息");
            createNotificationGroup(context, GROUP_PUSH_MESSAGE_ID, "推送消息");

            // 创建渠道
            // 下载完成通知栏
            createNotificationChannel(context,
                    GROUP_DOWNLOAD_ID,
                    CHANNEL_DOWNLOAD_COMPLETE_ID,
                    "下载完成通知栏",
                    NotificationManager.IMPORTANCE_MAX);

            // 下载通知栏
            createNotificationChannel(context,
                    GROUP_DOWNLOAD_ID,
                    CHANNEL_DOWNLOADING_ID,
                    "下载通知栏",
                    NotificationManager.IMPORTANCE_LOW);

            // 常规通知栏
            createNotificationChannel(context,
                    GROUP_PUSH_MESSAGE_ID,
                    CHANNEL_MESSAGE_ID,
                    "常规通知栏",
                    NotificationManager.IMPORTANCE_MAX);

        }
    }

    /**
     * 创建消息渠道组
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationGroup(Context context, String groupId, String groupName) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannelGroup group = new NotificationChannelGroup(groupId, groupName);
        if (notificationManager != null) {
            notificationManager.createNotificationChannelGroup(group);
        }
    }

    /**
     * 创建消息渠道，并分配到指定组下。
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    public static void createNotificationChannel(Context context, String groupId, String channelId, String channelName, int importance) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        NotificationChannel channel = new NotificationChannel(channelId, channelName, importance);
        channel.canShowBadge();
        //在创建通知渠道前，指定渠道组 id
        channel.setGroup(groupId);
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(channel);
        }
    }

}
