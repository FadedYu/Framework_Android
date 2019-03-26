package com.bonait.bnframework;

import android.Manifest;
import android.content.Context;
import android.os.Bundle;

import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.qmuiteam.qmui.widget.roundwidget.QMUIRoundButton;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity {

    @BindView(R.id.button)
    QMUIRoundButton button;

    private Context context;
    private String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA};
    private int mCurrentDialogStyle = com.qmuiteam.qmui.R.style.QMUI_Dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        context = this;

    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        checkPermission();
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
        }else {
            //未获取权限或拒绝权限时
            EasyPermissions.requestPermissions(this,
                    "xxx需要用到以下权限：\n\n1. 录制音频权限\n\n2. 录制视频权限\n\n3. 文件读取存储权限",
                    Constants.ALL_PERMISSION, params);
        }
    }
}
