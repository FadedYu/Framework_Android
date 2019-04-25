package com.bonait.bnframework.manager;

import android.app.Activity;
import android.app.Application;
import android.content.pm.ActivityInfo;
import android.os.Bundle;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by LY on 2019/4/3.
 */
public class ActivityLifecycleManager implements Application.ActivityLifecycleCallbacks  {

    /**
     * 维护Activity 的list
     */
    private static List<Activity> activityList = Collections.synchronizedList(new LinkedList<Activity>());
    //private List<Activity> activityList = new LinkedList<Activity>();


    /*单例模式静态内部类*/
    private static class SingletonHolder{
        private static ActivityLifecycleManager instance = new ActivityLifecycleManager();
    }
    private ActivityLifecycleManager(){
    }

    public static ActivityLifecycleManager get(){
        return SingletonHolder.instance;
    }

    /**
     * 开启ActivityLifecycleCallbacks接口回调
     */
    public void init(Application application){
        application.registerActivityLifecycleCallbacks(get());
    }



    /**
     * @param activity 作用说明 ：添加一个activity到管理里
     */
    public void pushActivity(Activity activity) {
        activityList.add(activity);
    }

    /**
     * @param activity 作用说明 ：删除一个activity在管理里
     */
    public void popActivity(Activity activity) {
        activityList.remove(activity);
    }

    /**
     * get current Activity 获取当前Activity（栈中最后一个压入的）
     */
    public Activity currentActivity() {
        if (activityList == null|| activityList.isEmpty()) {
            return null;
        }
        return activityList.get(activityList.size()-1);
    }

    /**
     * 结束当前Activity（栈中最后一个压入的）
     */
    public void finishCurrentActivity() {
        if (activityList == null|| activityList.isEmpty()) {
            return;
        }
        Activity activity = activityList.get(activityList.size()-1);
        finishActivity(activity);
    }

    /**
     * 结束指定的Activity
     */
    public void finishActivity(Activity activity) {
        if (activityList == null|| activityList.isEmpty()) {
            return;
        }
        if (activity != null) {
            activityList.remove(activity);
            activity.finish();
            activity = null;
        }
    }

    /**
     * 结束指定类名的Activity
     */
    public void finishActivity(Class<?> cls) {
        if (activityList == null|| activityList.isEmpty()) {
            return;
        }
        for (Activity activity : activityList) {
            if (activity.getClass().equals(cls)) {
                finishActivity(activity);
            }
        }
    }

    /**
     * 按照指定类名找到activity
     *
     */
    public Activity findActivity(Class<?> cls) {
        Activity targetActivity = null;
        if (activityList != null) {
            for (Activity activity : activityList) {
                if (activity.getClass().equals(cls)) {
                    targetActivity = activity;
                    break;
                }
            }
        }
        return targetActivity;
    }

    /**
     * @return 作用说明 ：获取当前最顶部activity的实例
     */
    public Activity getTopActivity() {
        Activity mBaseActivity;
        synchronized (activityList) {
            final int size = activityList.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = activityList.get(size);
        }
        return mBaseActivity;

    }

    /**
     * @return 作用说明 ：获取当前最顶部的acitivity 名字
     */
    public String getTopActivityName() {
        Activity mBaseActivity;
        synchronized (activityList) {
            final int size = activityList.size() - 1;
            if (size < 0) {
                return null;
            }
            mBaseActivity = activityList.get(size);
        }
        return mBaseActivity.getClass().getName();
    }

    /**
     * 结束所有Activity
     */
    public void finishAllActivity() {
        if (activityList == null) {
            return;
        }
        for (Activity activity : activityList) {
            activity.finish();
        }
        activityList.clear();
    }

    /**
     * 退出应用程序
     */
    public void appExit() {
        try {
            finishAllActivity();
            System.exit(0);
        } catch (Exception e) {
            e.getStackTrace();
        }
    }


    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /*
         *  监听到 Activity创建事件 将该 Activity 加入list
         */
        pushActivity(activity);
        // 设置禁止随屏幕旋转界面
        activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        if (null==activityList||activityList.isEmpty()) {
            return;
        }
        if (activityList.contains(activity)) {
            /*
             *  监听到 Activity销毁事件 将该Activity 从list中移除
             */
            popActivity(activity);
        }

        //横竖屏切换或配置改变时, Activity 会被重新创建实例, 但 Bundle 中的基础数据会被保存下来,移除该数据是为了保证重新创建的实例可以正常工作
        // 暂时没用到保存toolbar
        //activity.getIntent().removeExtra("isInitToolbar");
    }
}
