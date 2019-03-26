package com.bonait.bnframework.common.constant;

/**
 * Created by LY on 2019/3/21.
 */
public interface Constants {

    /*
     * 服务器IP地址，IP地址改变，在这里修改
     * 本:http://192.168.18.138:8080/GKIA
     * 外:http://113.108.201.18:23104/GKIA
     * http://192.168.22.98:23104/GKIA
     * */
    String SERVICE_IP = "http://113.108.201.18:23104/GKIA";

    // OkGo 连接超时时间,毫秒ms
    long CONNECT_TIME_OUT = 30000;

    // 申请权限跳转到设置界面code
    int APP_SETTING_DIALOG_REQUEST_CODE = 1;

    // 申请权限code
    int ALL_PERMISSION = 100;

}
