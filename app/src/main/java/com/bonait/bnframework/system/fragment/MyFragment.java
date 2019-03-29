package com.bonait.bnframework.system.fragment;

import android.view.LayoutInflater;
import android.view.View;

import com.bonait.bnframework.R;
import com.bonait.bnframework.common.base.BaseFragment;

public class MyFragment extends BaseFragment {


    public MyFragment() {
        // Required empty public constructor
    }

    @Override
    protected View onCreateView() {
        View root = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_my, null);
        return root;
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
