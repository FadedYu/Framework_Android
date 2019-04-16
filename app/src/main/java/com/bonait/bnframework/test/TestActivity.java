package com.bonait.bnframework.test;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.http.callback.files.FileProgressDialogCallBack;
import com.bonait.bnframework.common.http.callback.json.JsonDialogCallback;
import com.bonait.bnframework.common.http.callback.files2.FileProgressDialogCallBack2;
import com.bonait.bnframework.common.model.BaseCodeJson;
import com.bonait.bnframework.common.utils.AppUtils;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.bonait.bnframework.system.model.AppLoginPo;
import com.bonait.bnframework.system.model.UpdateAppPo;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class TestActivity extends BaseActivity {

    @BindView(R.id.button)
    QMUIRoundButton button;
    @BindView(R.id.button2)
    QMUIRoundButton button2;

    private String token = "";
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        ButterKnife.bind(this);
        context = this;
    }

    @OnClick({R.id.button, R.id.button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.button:
                ToastUtils.info("提交成功！");
                //SmartToast.info("提交成功！");
                //login();
                break;
            case R.id.button2:
                //SmartToast.warning("保存失败！");
                //ToastUtils.warning("保存失败！");
                // token未过期，跳转到主界面
                /*Intent intent = new Intent(TestActivity.this, BottomNavigation2Activity.class);
                startActivity(intent);*/
                //checkToken();
                checkPermission();
                break;
        }
    }


    private void checkToken() {
        String url = Constants.SERVICE_IP + "/checkToken.do";

        OkGo.<BaseCodeJson<Void>>post(url)
                .tag(this)
                .params("token", token)
                .execute(new JsonDialogCallback<BaseCodeJson<Void>>(this) {
                    @Override
                    public void onSuccess(Response<BaseCodeJson<Void>> response) {
                        BaseCodeJson<Void> baseCodeJson = response.body();
                        ToastUtils.info(baseCodeJson.getMsg());
                    }
                });
    }


    private void login() {
        String userName = "admin";
        String passWord = AppUtils.encryptSha256("1");
        String url = Constants.SERVICE_IP + "/appLogin.do";
        OkGo.<BaseCodeJson<AppLoginPo>>post(url)
                .tag(this)
                .params("username",userName)
                .params("password",passWord)
                .execute(new JsonDialogCallback<BaseCodeJson<AppLoginPo>>(this) {
                    @Override
                    public void onSuccess(Response<BaseCodeJson<AppLoginPo>> response) {
                        BaseCodeJson<AppLoginPo> loginPo = response.body();
                        token = loginPo.getResult().getToken();
                    }
                });
    }

    /**
     * 检查权限是否授权
     */
    @AfterPermissionGranted(Constants.ALL_PERMISSION)
    @Override
    public void checkPermission() {

        //String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
        String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        //判断是否获取权限
        if (EasyPermissions.hasPermissions(this, params)) {
            // 全部权限申请成功后
            getAppId();
        } else {
            //未获取权限或拒绝权限时
            EasyPermissions.requestPermissions(this,
                    "xxx需要用到以下权限：\n\n1. 录制音频权限\n\n2. 录制视频权限\n\n3. 文件读取存储权限",
                    Constants.ALL_PERMISSION, params);
        }
    }

    private String downloadUrl;
    private long totalSize;

    private void getAppId() {
        String url = Constants.SERVICE_IP+"/iandroid/appVersionAction!getNewVersion.do";
        OkGo.<UpdateAppPo>post(url)
                .tag(this)
                .execute(new JsonDialogCallback<UpdateAppPo>(this) {
                    @Override
                    public void onSuccess(Response<UpdateAppPo> response) {
                        UpdateAppPo updateAppPo = response.body();
                        String appId = updateAppPo.getApkId();
                        //获取apk下载地址
                        downloadUrl = Constants.SERVICE_IP+ "/file-download?fileId=" + appId;
                        totalSize = Long.parseLong(updateAppPo.getFileSize());
                        //toDownload();
                        //doDownload();
                    }
                });
    }

    private void toDownload() {
        OkGo.<File>get(downloadUrl)
                .tag(this)
                .execute(new FileProgressDialogCallBack2(this,totalSize) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        ToastUtils.success("下载完成！");
                    }
                });
    }

    private void doDownload() {
        OkGo.<File>get(downloadUrl)
                .tag(this)
                .execute(new FileProgressDialogCallBack(this) {
                    @Override
                    public void onSuccess(Response<File> response) {
                        ToastUtils.success("下载完成！");
                    }
                });
    }
}
