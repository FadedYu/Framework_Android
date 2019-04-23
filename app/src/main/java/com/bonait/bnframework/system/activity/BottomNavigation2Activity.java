package com.bonait.bnframework.system.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.MenuItem;

import com.bonait.bnframework.R;
import com.bonait.bnframework.application.ActivityLifecycleManager;
import com.bonait.bnframework.common.base.BaseActivity;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.bonait.bnframework.home.fragment.Home1Fragment;
import com.bonait.bnframework.home.fragment.Home2Fragment;
import com.bonait.bnframework.system.adapter.FragmentAdapter;
import com.bonait.bnframework.system.fragment.MyFragment;
import com.lzy.okgo.OkGo;
import com.qmuiteam.qmui.widget.QMUIViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BottomNavigation2Activity extends BaseActivity {


    @BindView(R.id.navigation)
    BottomNavigationView bottomNavigationView;
    @BindView(R.id.viewpager)
    QMUIViewPager viewPager;

    private MenuItem menuItem;
    private long exitTime = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation2);
        ButterKnife.bind(this);

        initFragment();
        viewPager.addOnPageChangeListener(pageChangeListener);
        // 设置viewPager缓存多少个fragment
        viewPager.setOffscreenPageLimit(3);
        bottomNavigationView.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    /**
     * viewPager里添加fragment
     */
    private void initFragment() {
        FragmentAdapter fragmentAdapter = new FragmentAdapter(getSupportFragmentManager());
        fragmentAdapter.addFragment(new Home1Fragment());
        fragmentAdapter.addFragment(new Home2Fragment());
        fragmentAdapter.addFragment(new MyFragment());
        viewPager.setAdapter(fragmentAdapter);
    }

    //-------------------------配置viewPager与fragment关联----------------------------//

    /**
     * 配置bottom底部菜单栏监听器，手指点击底部菜单监听
     */
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.bottom_navigation_1:
                    viewPager.setCurrentItem(0);
                    return true;
                case R.id.bottom_navigation_2:
                    viewPager.setCurrentItem(1);
                    return true;
                case R.id.bottom_navigation_3:
                    viewPager.setCurrentItem(2);
                    return true;
            }
            return false;
        }
    };


    /**
     * 配置ViewPager监听器，手指滑动监听
     */
    private ViewPager.OnPageChangeListener pageChangeListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            menuItem = bottomNavigationView.getMenu().getItem(position);
            menuItem.setChecked(true);
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };

    @Override
    protected boolean canDragBack() {
        return viewPager.getCurrentItem() == 0;
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
