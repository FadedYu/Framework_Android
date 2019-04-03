package com.bonait.bnframework.system.activity;

import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LinearInterpolator;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.utils.AnimationToolUtils;
import com.bonait.bnframework.common.utils.KeyboardToolUtils;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.qmuiteam.qmui.util.QMUIStatusBarHelper;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends BaseActivity {

    @BindView(R.id.logo)
    ImageView mLogo;
    @BindView(R.id.et_account)
    EditText mEtAccount;
    @BindView(R.id.iv_clean_account)
    ImageView mIvCleanAccount;
    @BindView(R.id.et_password)
    EditText mEtPassword;
    @BindView(R.id.clean_password)
    ImageView mCleanPassword;
    @BindView(R.id.iv_show_pwd)
    ImageView mIvShowPwd;
    @BindView(R.id.btn_login)
    Button mBtnLogin;
    @BindView(R.id.cb_checkbox)
    CheckBox cbCheckbox;
    @BindView(R.id.forget_password)
    Button mForgetPassword;
    @BindView(R.id.content)
    LinearLayout mContent;
    @BindView(R.id.scrollView)
    NestedScrollView mScrollView;
    @BindView(R.id.root)
    RelativeLayout mRoot;

    private int screenHeight = 0;//屏幕高度
    private int keyHeight = 0; //软件盘弹起后所占高度
    private final float scale = 0.9f; //logo缩放比例
    private int height = 0;

    boolean isCheckBox;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        QMUIStatusBarHelper.setStatusBarLightMode(this);
        initView();
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
                // 隐藏软键盘
                KeyboardToolUtils.hideSoftInput(LoginActivity.this);
                // 退出界面之前把状态栏还原为白色字体与图标
                QMUIStatusBarHelper.setStatusBarDarkMode(LoginActivity.this);
                Intent intent = new Intent(LoginActivity.this, BottomNavigation2Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
                LoginActivity.this.finish();
                break;
        }
    }

    private void initView() {

    }

    /**
     * 忘记密码
     * */
    private void forgotPassword() {
        ToastUtils.info("请与管理员联系修改密码！");
    }

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
        /*String pwd = mEtPassword.getText().toString();
        if (!TextUtils.isEmpty(pwd))
            mEtPassword.setSelection(pwd.length());*/
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initEvent() {

        // 获取屏幕高度
        screenHeight = this.getResources().getDisplayMetrics().heightPixels;
        // 弹起高度为屏幕高度的1/3
        keyHeight = screenHeight / 3;

        // 输入账号状态监听，在右边显示或隐藏clean
        addIconClearListener(mEtAccount,mIvCleanAccount);
        // 监听EtPassword输入状态
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
}
