package com.bonait.bnframework.common.constant;

import com.bonait.bnframework.R;
import com.bonait.bnframework.application.MainApplication;

/**
 * Created by LY on 2019/3/21.
 */
public interface Constants {

    /*
     * 服务器IP地址，IP地址改变，在这里修改
     * 本:http://192.168.18.138:8080/GKIA
     * 外:http://113.108.201.18:23104/GKIA
     * 内:http://192.168.22.98:23104/GKIA
     * */
    String SERVICE_IP = "http://192.168.18.139:8080/GKIA";
    String APP_TOKEN = "appToken";

    // admin测试数据使用
    boolean SKIP_TO_TEST_ACTIVITY = true; // 是否启用TestActivity
    boolean superAdminTest = MainApplication.getContext().getResources().getBoolean(R.bool.superAdminTest);

    // OkGo 连接超时时间,毫秒ms
    long CONNECT_TIME_OUT = 6000;

    // 申请权限跳转到设置界面code
    int APP_SETTING_DIALOG_REQUEST_CODE = 1;

    // 申请权限code
    int ALL_PERMISSION = 100;

    // 更新apk
    int UPDATE_APP = 102;
    int INSTALL_PERMISSION_CODE = 103;

}
