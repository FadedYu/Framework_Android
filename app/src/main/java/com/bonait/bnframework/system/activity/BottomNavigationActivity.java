package com.bonait.bnframework.system.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import com.bonait.bnframework.R;
import com.bonait.bnframework.application.ActivityLifecycleManager;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.bonait.bnframework.home.fragment.Home1Fragment;
import com.bonait.bnframework.home.fragment.Home2Fragment;
import com.bonait.bnframework.system.fragment.MyFragment;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.util.QMUIResHelper;
import com.qmuiteam.qmui.widget.QMUIPagerAdapter;
import com.qmuiteam.qmui.widget.QMUITabSegment;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomNavigationActivity extends BaseActivity {

    @BindView(R.id.main_view_pager)
    QMUIViewPager mViewPager;
    @BindView(R.id.main_tabs)
    QMUITabSegment mTabSegment;

    private Context context;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation);
        ButterKnife.bind(this);

        context = this;

        // 初始化tabs
        initTabs();
        // 初始化viewPager，并填充fragment
        initPagers();
    }

    /**
     * 初始化tab
     * */
    private void initTabs() {
        int normalColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_gray_6);
        int selectColor = QMUIResHelper.getAttrColor(context, R.attr.qmui_config_color_blue);
        mTabSegment.setDefaultNormalColor(normalColor);
        mTabSegment.setDefaultSelectedColor(selectColor);

        QMUITabSegment.Tab home = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_component),
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_component_selected),
                "主页", false
        );

        QMUITabSegment.Tab Lab = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_util),
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_util_selected),
                "功能", false
        );
        QMUITabSegment.Tab My = new QMUITabSegment.Tab(
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_lab),
                ContextCompat.getDrawable(context, R.mipmap.icon_tabbar_lab_selected),
                "我的", false
        );
        mTabSegment.addTab(home)
                .addTab(Lab)
                .addTab(My);
    }

    /**
     * 初始化viewPager并添加fragment
     * */
    private void initPagers() {
        QMUIPagerAdapter pagerAdapter = new QMUIPagerAdapter() {
            private FragmentTransaction mCurrentTransaction;
            private Fragment mCurrentPrimaryItem = null;

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == ((Fragment) object).getView();
            }

            @Override
            public int getCount() {
                return 3;
            }

            @SuppressLint("CommitTransaction")
            @Override
            protected Object hydrate(ViewGroup container, int position) {
                String name = makeFragmentName(container.getId(), position);
                if (mCurrentTransaction == null) {
                    mCurrentTransaction = getSupportFragmentManager()
                            .beginTransaction();
                }
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
                if(fragment != null){
                    return fragment;
                }
                switch (position) {
                    case 0:
                        return new Home1Fragment();
                    case 1:
                        return new Home2Fragment();
                    case 2:
                        return new MyFragment();
                    default:
                        return new Home1Fragment();
                }
            }

            @SuppressLint("CommitTransaction")
            @Override
            protected void populate(ViewGroup container, Object item, int position) {
                String name = makeFragmentName(container.getId(), position);
                if (mCurrentTransaction == null) {
                    mCurrentTransaction = getSupportFragmentManager()
                            .beginTransaction();
                }
                Fragment fragment = getSupportFragmentManager().findFragmentByTag(name);
                if (fragment != null) {
                    mCurrentTransaction.attach(fragment);
                    if(fragment.getView() != null && fragment.getView().getWidth() == 0){
                        fragment.getView().requestLayout();
                    }
                } else {
                    fragment = (Fragment) item;
                    mCurrentTransaction.add(container.getId(), fragment, name);
                }
                if (fragment != mCurrentPrimaryItem) {
                    fragment.setMenuVisibility(false);
                    fragment.setUserVisibleHint(false);
                }
            }

            @SuppressLint("CommitTransaction")
            @Override
            protected void destroy(ViewGroup container, int position, Object object) {
                if (mCurrentTransaction == null) {
                    mCurrentTransaction = getSupportFragmentManager()
                            .beginTransaction();
                }
                mCurrentTransaction.detach((Fragment) object);
            }

            @Override
            public void startUpdate(ViewGroup container) {
                if (container.getId() == View.NO_ID) {
                    throw new IllegalStateException("ViewPager with adapter " + this
                            + " requires a view id");
                }
            }

            @Override
            public void finishUpdate(ViewGroup container) {
                if (mCurrentTransaction != null) {
                    mCurrentTransaction.commitNowAllowingStateLoss();
                    mCurrentTransaction = null;
                }
            }

            @Override
            public void setPrimaryItem(ViewGroup container, int position, Object object) {
                Fragment fragment = (Fragment) object;
                if (fragment != mCurrentPrimaryItem) {
                    if (mCurrentPrimaryItem != null) {
                        mCurrentPrimaryItem.setMenuVisibility(false);
                        mCurrentPrimaryItem.setUserVisibleHint(false);
                    }
                    if (fragment != null) {
                        fragment.setMenuVisibility(true);
                        fragment.setUserVisibleHint(true);
                    }
                    mCurrentPrimaryItem = fragment;
                }
            }

            private String makeFragmentName(int viewId, long id) {
                return BottomNavigationActivity.class.getSimpleName() + ":" + viewId + ":" + id;
            }
        };
        mViewPager.setAdapter(pagerAdapter);
        mTabSegment.setupWithViewPager(mViewPager,false);
    }

    @Override
    protected boolean canDragBack() {
        return mViewPager.getCurrentItem() == 0;
    }

    /**
     * 重写返回键，实现双击退出程序效果
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (System.currentTimeMillis() - exitTime > 2000) {
                ToastUtils.normal("再按一次退出程序");
                exitTime = System.currentTimeMillis();
            } else {
                OkGo.getInstance().cancelAll();
                overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                ActivityLifecycleManager.get().appExit();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
