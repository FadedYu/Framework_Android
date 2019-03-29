package com.bonait.bnframework.home.fragment;


import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseFragment;
import com.bonait.bnframework.common.utils.ToastUtils;
import com.orhanobut.logger.Logger;
import com.qmuiteam.qmui.widget.QMUITopBarLayout;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * A simple {@link Fragment} subclass.
 */
public class Home3Fragment extends BaseFragment {

    @BindView(R.id.topbar)
    QMUITopBarLayout mTopBar;

    public Home3Fragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_home3, null);
        ButterKnife.bind(this, root);
        Logger.d("第三页创建");

        initTopBar();
        return root;
    }

    private void initTopBar() {
        mTopBar.setTitle("第三页");
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        ToastUtils.info("我的");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Logger.d("第三页销毁");
    }

    /**
     * 当在activity设置viewPager + BottomNavigation + fragment时，
     * 为防止viewPager左滑动切换界面，与fragment左滑返回上一界面冲突引起闪退问题，
     * 必须加上此方法，禁止fragment左滑返回上一界面。
     *
     * 切记！切记！切记！否则会闪退！
     *
     * 在fragment设置viewPager则不会出现这个问题。
     * */
    @Override
    protected boolean canDragBack() {
        return false;
    }
}
