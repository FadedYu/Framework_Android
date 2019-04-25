package com.bonait.bnframework.modules.mine.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseFragment;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.utils.AlertDialogUtils;
import com.bonait.bnframework.common.utils.UpdateAppUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.dialog.QMUIDialog;
import com.qmuiteam.qmui.widget.dialog.QMUIDialogAction;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.EasyPermissions;

public class MyFragment extends BaseFragment {


    @BindView(R.id.h_background)
    ImageView hBackground;
    @BindView(R.id.h_head)
    ImageView hHead;
    @BindView(R.id.h_user_name)
    TextView hUserName;
    @BindView(R.id.stv_change_pwd)
    SuperTextView stvChangePwd;
    @BindView(R.id.stv_update)
    SuperTextView stvUpdate;
    @BindView(R.id.stv_logout)
    SuperTextView stvLogout;

    /*
    private static final String BUNDLE_TITLE = "key_title";
    public static MyFragment newInstance(String title) {
        Bundle bundle = new Bundle();
        bundle.putString(BUNDLE_TITLE,title);
        MyFragment myFragment = new MyFragment();
        myFragment.setArguments(bundle);
        return myFragment;
    }
    */

    private Context context;

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, null);
        ButterKnife.bind(this, root);

        return root;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Logger.d("我的fragment创建");
        context = getContext();
        initView();
    }

    @OnClick(R.id.h_head)
    public void onViewClicked() {

    }

    private void initView() {

        /*
         * 版本更新，点击事件
         * */
        stvUpdate.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                //检查权限，并启动版本更新
                checkPermission();
            }
        });
    }



    /***********************************检查App更新********************************************/
    @AfterPermissionGranted(Constants.UPDATE_APP)
    @Override
    public void checkPermission() {
        // 检查文件读写权限
        String[] params = {Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (EasyPermissions.hasPermissions(context,params)) {

            //Android 8.0后，安装应用需要检查打开未知来源应用权限
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                checkInstallPermission();
            } else {
                UpdateAppUtils.updateApp(context);
            }
        } else {
            //未获取权限
            EasyPermissions.requestPermissions(this, "更新版本需要读写本地权限！", Constants.UPDATE_APP, params);
        }
    }

    /**
     * Android 8.0后，安装应用需要检查打开未知来源应用权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void checkInstallPermission() {
        // 判断是否已打开未知来源应用权限
        boolean haveInstallPermission = context.getPackageManager().canRequestPackageInstalls();

        if (haveInstallPermission) {
            //已经打开权限，直接启动版本更新
            UpdateAppUtils.updateApp(context);
        } else {
            AlertDialogUtils.showDialog(getContext(),
                    "请打开未知来源应用权限",
                    "为了正常升级APP，请点击设置-高级设置-允许安装未知来源应用，本功能只限用于APP版本升级。",
                    "权限设置",
                    new QMUIDialogAction.ActionListener() {
                        @Override
                        public void onClick(QMUIDialog dialog, int index) {
                            // 跳转到系统打开未知来源应用权限，在onActivityResult中启动更新
                            toInstallPermissionSettingIntent(context);
                            dialog.dismiss();
                        }
                    });
        }
    }

    /**
     * 开启安装未知来源权限
     */
    @RequiresApi(api = Build.VERSION_CODES.O)
    private void toInstallPermissionSettingIntent(Context context) {
        Uri packageURI = Uri.parse("package:" + context.getPackageName());
        Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES, packageURI);
        startActivityForResult(intent, Constants.INSTALL_PERMISSION_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case Constants.INSTALL_PERMISSION_CODE:
                checkPermission();
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("我的fragment销毁");
    }

    /**
     * 当在activity设置viewPager + BottomNavigation + fragment时，
     * 为防止viewPager左滑动切换界面，与fragment左滑返回上一界面冲突引起闪退问题，
     * 必须加上此方法，禁止fragment左滑返回上一界面。
     *
     * 切记！切记！切记！否则会闪退！
     *
     * 当在fragment设置viewPager + BottomNavigation + fragment时，则不会出现这个问题。
     */
    @Override
    protected boolean canDragBack() {
        return false;
    }

}
