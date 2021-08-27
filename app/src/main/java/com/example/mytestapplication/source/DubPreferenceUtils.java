package com.example.mytestapplication.source;


import android.content.Context;
import android.content.SharedPreferences;

/**
 * 共享参数工具类
 */
public class DubPreferenceUtils {
    private static final String SHARED_PATH = "app_share";

    public static SharedPreferences getDefaultSharedPreferences(Context context) {
        return (null == context) ? null : context.getSharedPreferences(SHARED_PATH, Context.MODE_PRIVATE);
    }

    public static void putInt(Context context, String key, int value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putInt(key, value);
        edit.commit();
        //sharedPreferences.edit().putInt(key, value).commit();
    }

    public static int getInt(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return -1;
        return sharedPreferences.getInt(key,-1);
    }

    public static void putString(Context context, String key, String value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putString(key, value);
        edit.commit();
        //sharedPreferences.edit().putString(key, value).commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return null;
        return sharedPreferences.getString(key,null);
    }

    public static void remove(Context context, String... keys) {
        if(null == keys)return;
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        for(String key : keys)edit.remove(key);
        edit.commit();
        //sharedPreferences.edit().putString(key, value).commit();
    }

    public static void clearPreference(Context context, final SharedPreferences p) {
        final SharedPreferences.Editor editor = p.edit();
        editor.clear();
        editor.commit();
    }

    public static void putLong(Context context, String key, long value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putLong(key, value);
        edit.commit();
    }

    public static long getLong(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return -1;
        return sharedPreferences.getLong(key,-1);
    }

    public static void putFloat(Context context, String key, Float value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putFloat(key, value);
        edit.commit();
    }

    public static Float getFloat(Context context, String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return -1f;
        return sharedPreferences.getFloat(key,-1);
    }

    public static void putBoolean(Context context, String key, boolean value) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return;
        SharedPreferences.Editor edit = sharedPreferences.edit();
        edit.putBoolean(key, value);
        edit.commit();
        //sharedPreferences.edit().putBoolean(key, defuVal).commit();
    }

    public static boolean getBoolean(Context context, String key, boolean defValue) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return false;
        return sharedPreferences.getBoolean(key,defValue);
    }

    public static boolean contains(Context context,String key) {
        SharedPreferences sharedPreferences = getDefaultSharedPreferences(context);
        if(null == sharedPreferences)return false;
        return sharedPreferences.contains(key);
    }

    /* 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法 */
    public static void setParam(Context context, String key, Object object) {
        String type = object.getClass().getSimpleName();
        SharedPreferences sp = getDefaultSharedPreferences(context);
        if(null == sp)return;
        SharedPreferences.Editor editor = sp.edit();
        if ("String".equals(type)) {
            editor.putString(key, (String) object);
        } else if ("Integer".equals(type)) {
            editor.putInt(key, (Integer) object);
        } else if ("Boolean".equals(type)) {
            editor.putBoolean(key, (Boolean) object);
        } else if ("Float".equals(type)) {
            editor.putFloat(key, (Float) object);
        } else if ("Long".equals(type)) {
            editor.putLong(key, (Long) object);
        }
        editor.commit();
    }

    /* 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值 */
    public static Object getParam(Context context, String key, Object defaultObject) {
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = getDefaultSharedPreferences(context);
        if(null == sp)return null;
        if ("String".equals(type)) {
            return sp.getString(key, (String) defaultObject);
        } else if ("Integer".equals(type)) {
            return sp.getInt(key, (Integer) defaultObject);
        } else if ("Boolean".equals(type)) {
            return sp.getBoolean(key, (Boolean) defaultObject);
        } else if ("Float".equals(type)) {
            return sp.getFloat(key, (Float) defaultObject);
        } else if ("Long".equals(type)) {
            return sp.getLong(key, (Long) defaultObject);
        }
        return null;
    }
}
