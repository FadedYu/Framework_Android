package com.bonait.bnframework.test;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.http.DialogCallback;
import com.bonait.bnframework.common.model.BaseCodeJson;
import com.bonait.bnframework.common.model.LoginPo;
import com.bonait.bnframework.common.utils.AppUtils;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.Response;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

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
    private String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};

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
                login();
                break;
            case R.id.button2:
                checkToken();
                break;
        }
    }


    private void checkToken() {
        String url = Constants.SERVICE_IP + "/checkToken.do";

        OkGo.<BaseCodeJson<Void>>post(url)
                .tag(this)
                .params("token", token)
                .execute(new DialogCallback<BaseCodeJson<Void>>(this) {
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
        OkGo.<BaseCodeJson<LoginPo>>post(url)
                .tag(this)
                .params("username",userName)
                .params("password",passWord)
                .execute(new DialogCallback<BaseCodeJson<LoginPo>>(this) {
                    @Override
                    public void onSuccess(Response<BaseCodeJson<LoginPo>> response) {
                        BaseCodeJson<LoginPo> loginPo = response.body();
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

        //判断是否获取存储读写权限
        if (EasyPermissions.hasPermissions(this, params)) {
            // 显示列表操作
            ToastUtils.info("全部申请成功");
        } else {
            //未获取权限或拒绝权限时
            EasyPermissions.requestPermissions(this,
                    "xxx需要用到以下权限：\n\n1. 录制音频权限\n\n2. 录制视频权限\n\n3. 文件读取存储权限",
                    Constants.ALL_PERMISSION, params);
        }
    }
}
