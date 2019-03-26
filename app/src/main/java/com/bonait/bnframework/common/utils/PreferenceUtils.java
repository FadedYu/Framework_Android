package com.bonait.bnframework.common.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by LY on 2019/3/21.
 */
public class PreferenceUtils {
    private static SharedPreferences sharedPreferences;

    /**
     * 初始化SharedPreferences
     * */
    public static void initPreference(Context context,String name,int mode) {
        if (sharedPreferences != null) {
            sharedPreferences = context.getSharedPreferences(name, mode);
        }
    }

    /**
     * 存String
     * @param key  键
     * @param value  值
     */
    public static void  setString(String key, String value) {
        sharedPreferences.edit().putString(key,value).apply();
    }

    /**
     * 取String
     * @param key   键
     * @param devalue  默认值
     * @return String
     */
    public static String getString(String key, String devalue) {
        return sharedPreferences.getString(key,devalue);
    }

    /**
     * 存Float
     * @param key  键
     * @param value  值
     */
    public static void setFloat(String key, float value) {
        sharedPreferences.edit().putFloat(key,value).apply();
    }

    /**
     * 取Float
     * @param key   键
     * @param devalue  默认值
     * @return Float
     */
    public static Float getFloat(String key, float devalue){
        return  sharedPreferences.getFloat(key,devalue);
    }

    /**
     * 存boolean
     * @param key  键
     * @param value  值
     */
    public static void setBoolean(String key, boolean value) {
        sharedPreferences.edit().putBoolean(key,value).apply();
    }

    /**
     * 取boolean
     * @param key   键
     * @param defValue  默认值
     * @return Boolean
     */
    public static boolean getBoolean(String key, boolean defValue) {
        return sharedPreferences.getBoolean(key,defValue);
    }

    /**
     * 存int
     * @param key  键
     * @param value  值
     */
    public static void setInt(String key, int value) {
        sharedPreferences.edit().putInt(key,value).apply();
    }

    /**
     * 存int
     * @param key  键
     * @param value  值
     */
    public static void setInt(String key, Integer value) {
        sharedPreferences.edit().putInt(key,(Integer) value).apply();
    }

    /**
     * 取int
     * @param key   键
     * @param defValue  默认值
     * @return Int
     */
    public static int getInt(String key, int defValue) {
        return sharedPreferences.getInt(key,defValue);
    }

    /**
     * 存Long
     * @param key  键
     * @param value  值
     */
    public static void setLong(String key, int value) {
        sharedPreferences.edit().putLong(key,value).apply();
    }

    /**
     * 取Long
     * @param key   键
     * @param defValue  默认值
     * @return Long
     */
    public static Long getLong(String key, int defValue) {
        return sharedPreferences.getLong(key,defValue);
    }

    /**
     * 清空一个
     * @param key 键
     */
    public static void remove(String key) {
        sharedPreferences.edit().remove(key).apply();
    }

    /**
     * 清空所有
     */
    public static void removeAll() {
        sharedPreferences.edit().clear().apply();
    }
}
