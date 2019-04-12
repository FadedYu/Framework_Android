package com.bonait.bnframework.system.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.constant.Constants;
import com.bonait.bnframework.common.constant.SPConstants;
import com.bonait.bnframework.common.http.DialogCallback;
import com.bonait.bnframework.common.model.BaseCodeJson;
import com.bonait.bnframework.common.utils.AlertDialogUtils;
import com.bonait.bnframework.common.utils.AnimationToolUtils;
import com.bonait.bnframework.common.utils.AppUtils;
import com.bonait.bnframework.common.utils.KeyboardToolUtils;
import com.bonait.bnframework.common.utils.PreferenceUtils;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.bonait.bnframework.system.model.AppLoginPo;
import com.bonait.bnframework.test.TestActivity;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.model.Response;
import com.mobsandgeeks.saripaar.ValidationError;
import com.mobsandgeeks.saripaar.Validator;
import com.mobsandgeeks.saripaar.annotation.NotEmpty;
import com.mobsandgeeks.saripaar.annotation.Order;
import com.mobsandgeeks.saripaar.annotation.Password;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import org.litepal.LitePal;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity implements Validator.ValidationListener {

    @BindView(R.id.logo)
    ImageView mLogo;

    @Order(1)
    @NotEmpty(message = "用户名不能为空")
    @BindView(R.id.et_account)
    EditText mEtAccount;

    @Order(2)
    @NotEmpty(message = "密码不能为空")
    @Password(min = 1, scheme = Password.Scheme.ALPHA_NUMERIC_MIXED_CASE_SYMBOLS,message = "密码不能少于1位")
    @BindView(R.id.et_password)
    EditText mEtPassword;

    @BindView(R.id.iv_clean_account)
    ImageView mIvCleanAccount;
    @BindView(R.id.clean_password)
    ImageView mCleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView mIvShowPwd;

    @BindView(R.id.cb_checkbox)
    CheckBox cbCheckbox;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private final float scale = 0.9f; //logo缩放比例
    private int height = 0;
    boolean isCheckBox;

    private Validator validator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        QMUIStatusBarHelper.setStatusBarLightMode(this);

        validator = new Validator(this);
        validator.setValidationListener(this);

        initEvent();
    }

    @OnClick({R.id.iv_clean_account, R.id.clean_password, R.id.iv_show_pwd,R.id.forget_password, R.id.btn_login})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_clean_account:
                mEtAccount.setText("");
                mEtPassword.setText("");
                break;
            case R.id.clean_password:
                mEtPassword.setText("");
                break;
            case R.id.iv_show_pwd:
                changePasswordEye();
                break;
            case R.id.forget_password:
                forgotPassword();
                break;
            case R.id.btn_login:
                // 登录，启动表单验证，重写方法：onValidationSucceeded
                validator.validate();
                break;
        }
    }

    /**
     * 忘记密码
     * */
    private void forgotPassword() {
        ToastUtils.info("请与管理员联系修改密码！");
    }

    // *************************以下为登录验证及跳转界面相关*************************//

    /**
     * 登录表单验证成功后，请求后台验证账号密码
     * */
    private void attemptLogin() {
        String url = Constants.SERVICE_IP + "/appLogin.do";

        final String userAccount = mEtAccount.getText().toString();
        final String password = mEtPassword.getText().toString();
        //密码加密
        String newPassword = AppUtils.encryptSha256(password);

        OkGo.<BaseCodeJson<AppLoginPo>>post(url)
                .tag(this)
                .params("username",userAccount)
                .params("password",newPassword)
                .execute(new DialogCallback<BaseCodeJson<AppLoginPo>>(this) {
                    @Override
                    public void onSuccess(Response<BaseCodeJson<AppLoginPo>> response) {
                        BaseCodeJson<AppLoginPo> loginJson = response.body();
                        if (loginJson != null) {
                            whichDepartment(userAccount,password,loginJson.getResult());
                        }
                    }
                });
    }

    /**
     * 判断该账号是否有所属部门
     * */
    private void whichDepartment(String userAccount, String password, AppLoginPo appLoginPo) {
        String firstDepId = appLoginPo.getFirstDepId();

        if (firstDepId == null) {

            // 测试数据
            if (Constants.superAdminTest && 1 == appLoginPo.getId()) {
                // 测试数据
                superAdminTest(appLoginPo);
                // 存储用户必要的数据
                saveUserDataToSharePreference(userAccount, password,appLoginPo);
                //跳转到主页
                skipToMainActivity();
                return;
            }

            AlertDialogUtils.showDialog(
                    this,
                    "登录提示：",
                    "该账户未确认组织类型，无法登录，请与管理员联系！",
                    "重新登录");
        } else {

            // 测试数据
            if (Constants.superAdminTest) {
                superAdminTest(appLoginPo);
            }

            // 存储用户必要的数据
            saveUserDataToSharePreference(userAccount, password,appLoginPo);
            //跳转到主页
            skipToMainActivity();
        }

    }

    /**
     * 根据返回的结果，存储用户必要的数据到SharePreference
     * */
    private void saveUserDataToSharePreference(String userName, String password,AppLoginPo appLoginPo) {
        // 默认当返回数据没有猪场时，不保存
        // 当返回数据有猪场时，保存猪场id和猪场名字
        if (!"".equals(appLoginPo.getPigFarmId())) {
            PreferenceUtils.setString(SPConstants.PIG_FARM_ID,appLoginPo.getPigFarmId());
        }
        if (!"".equals(appLoginPo.getPigFarmName())) {
            PreferenceUtils.setString(SPConstants.PIG_FARM_NAME,appLoginPo.getPigFarmName());
        }
        //保存用户账号密码到sharePreference
        PreferenceUtils.setString(SPConstants.USER_NAME, userName);
        PreferenceUtils.remove(SPConstants.PASSWORD);
        // 如果用户点击了“记住密码”，保存密码
        if (isCheckBox) {
            PreferenceUtils.setString(SPConstants.PASSWORD, password);
        }
        // 保存用户名字
        PreferenceUtils.setString(SPConstants.USER, appLoginPo.getName());
        // 保存用户ID
        PreferenceUtils.setInt(SPConstants.USER_ID, appLoginPo.getId());
        // 保存角色
        PreferenceUtils.setString(SPConstants.ROLE_NAMES,appLoginPo.getRoleNames());

        // 保存部门
        if (appLoginPo.getFirstDepId() != null) {
            PreferenceUtils.setString(SPConstants.FIRST_DEP_ID,appLoginPo.getFirstDepId());
        }

        //保存token到sharePreference
        PreferenceUtils.setString(SPConstants.TOKEN, appLoginPo.getToken());
        //获取token
        HttpParams params = new HttpParams();
        params.put(Constants.APP_TOKEN, PreferenceUtils.getString(SPConstants.TOKEN, ""));
        OkGo.getInstance().init(getApplication())
                .addCommonParams(params);
        //启动本地数据库
        LitePal.getDatabase();
    }

    /**
     * 跳转到主界面
     * */
    private void skipToMainActivity() {
        // 隐藏软键盘
        KeyboardToolUtils.hideSoftInput(LoginActivity.this);
        // 退出界面之前把状态栏还原为白色字体与图标
        QMUIStatusBarHelper.setStatusBarDarkMode(LoginActivity.this);
        //Intent intent = new Intent(LoginActivity.this, BottomNavigation2Activity.class);
        Intent intent = new Intent(LoginActivity.this, TestActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
        LoginActivity.this.finish();
    }

    // *************************以上为登录验证及跳转界面相关*************************//

    /**
     * 测试数据
     * */
    private void superAdminTest(AppLoginPo appLoginPo) {
        int id = appLoginPo.getId();
        if (id == 1) {
            // 当返回数据没有猪场时，默认是五一猪场
            PreferenceUtils.setString(SPConstants.PIG_FARM_ID,"0");
            PreferenceUtils.setString(SPConstants.PIG_FARM_NAME,"五一猪场");
        }
    }


    //---------------------------------分割线-------------------------------------//

    /**
     * 点击眼睛图片显示或隐藏密码
     * */
    private void changePasswordEye() {
        if (mEtPassword.getInputType() != InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD) {
            mEtPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
            mIvShowPwd.setImageResource(R.drawable.icon_pass_visuable);
        } else {
            mEtPassword.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
            mIvShowPwd.setImageResource(R.drawable.icon_pass_gone);
        }

        // 将光标移至文字末尾
        String pwd = mEtPassword.getText().toString();
        if (!TextUtils.isEmpty(pwd))
            mEtPassword.setSelection(pwd.length());
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {

        // 获取屏幕高度
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        // 弹起高度为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        // 输入账号状态监听，在右边显示或隐藏clean
        addIconClearListener(mEtAccount,mIvCleanAccount);
        // 监听EtPassword输入状态，在右边显示或隐藏clean
        addIconClearListener(mEtPassword,mCleanPassword);

        /*
        * 记住密码Checkbox点击监听器
        * */
        cbCheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isCheckBox = isChecked;
            }
        });

        /*
         * 禁止键盘弹起的时候可以滚动
         */
        mScrollView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return true;
            }
        });
        // ScrollView监听滑动状态
        mScrollView.addOnLayoutChangeListener(new ViewGroup.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
              /* old是改变前的左上右下坐标点值，没有old的是改变后的左上右下坐标点值
              现在认为只要控件将Activity向上推的高度超过了1/3屏幕高，就认为软键盘弹起*/
                if (oldBottom != 0 && bottom != 0 && (oldBottom - bottom > keyHeight)) {
                    int dist = mContent.getBottom() - mScrollView.getHeight();
                    if (dist > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", 0.0f, -dist);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        AnimationToolUtils.zoomIn(mLogo, scale, dist);
                    }

                } else if (oldBottom != 0 && bottom != 0 && (bottom - oldBottom > keyHeight)) {
                    if ((mContent.getBottom() - oldBottom) > 0) {
                        ObjectAnimator mAnimatorTranslateY = ObjectAnimator.ofFloat(mContent, "translationY", mContent.getTranslationY(), 0);
                        mAnimatorTranslateY.setDuration(300);
                        mAnimatorTranslateY.setInterpolator(new LinearInterpolator());
                        mAnimatorTranslateY.start();
                        //键盘收回后，logo恢复原来大小，位置同样回到初始位置
                        AnimationToolUtils.zoomOut(mLogo, scale);
                    }
                }
            }
        });
    }

    /**
     * 设置文本框与右侧删除图标监听器
     */
    private void addIconClearListener(final EditText et, final ImageView iv) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                //如果文本框长度大于0，则显示删除图标，否则不显示
                if (s.length() > 0) {
                    iv.setVisibility(View.VISIBLE);
                } else {
                    iv.setVisibility(View.INVISIBLE);
                }
            }
        });
    }

    @Override
    public void onValidationSucceeded() {
        // 注解验证全部通过验证，开始后台验证
        attemptLogin();
    }

    @Override
    public void onValidationFailed(List<ValidationError> errors) {
        for (ValidationError error : errors) {
            View view = error.getView();
            String message = error.getCollatedErrorMessage(this);

            // 显示上面注解中添加的错误提示信息
            if (view instanceof EditText) {
                ((EditText) view).setError(message);
            } else {
                ToastUtils.error(message);
            }
        }
    }
}
