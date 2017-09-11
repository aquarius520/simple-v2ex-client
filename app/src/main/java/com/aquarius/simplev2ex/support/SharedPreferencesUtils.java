package com.aquarius.simplev2ex.support;

import android.content.Context;
import android.content.SharedPreferences;

import com.aquarius.simplev2ex.V2exApplication;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by aquarius on 2017/9/11.
 */
public class SharedPreferencesUtils {

    private static Context mContext;

    /**
     * 在MyApplication里初始化，避免再重复传Context
     * @param context
     */
    public static void init(Context context)
    {
        mContext = context;
    }


    /**
     * 获取偏好文件
     * @param spName
     */
    public static SharedPreferences getSharedPreferences(String spName)
    {
        return mContext.getSharedPreferences(spName,Context.MODE_PRIVATE);
    }



    /**
     * 保存在手机里面的文件名
     */
    public static final String FILE_NAME = "data";

    public static void setParam(String key, Object object) {
        setParam(V2exApplication.getInstance(), key, object);
    }

    public static Object getParam(String key, Object defaultObject) {
        return getParam(V2exApplication.getInstance(), key, defaultObject);
    }

    public static void remove(String key) {
        remove(V2exApplication.getInstance(), key);
    }

    /**
     * 保存数据的方法，我们需要拿到保存数据的具体类型，然后根据类型调用不同的保存方法
     * @param context
     * @param key
     * @param object
     */
    public static void setParam(Context context , String key, Object object){

        if (isEmpty(object)){
            return;
        }
//		String type = object.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();

        if(object instanceof String){
            editor.putString(key, (String) object);
        }
        else if(object instanceof Integer){
            editor.putInt(key, (Integer) object);
        }
        else if(object instanceof Boolean ){
            editor.putBoolean(key, (Boolean)object);
        }
        else if(object instanceof Float){
            editor.putFloat(key, (Float)object);
        }
        else if(object instanceof Long){
            editor.putLong(key, (Long)object);
        }

        editor.commit();
    }

    /**
     * 得到保存数据的方法，我们根据默认值得到保存的数据的具体类型，然后调用相对于的方法获取值
     * @param context
     * @param key
     * @param defaultObject
     * @return
     */
    public static Object getParam(Context context , String key, Object defaultObject){
        String type = defaultObject.getClass().getSimpleName();
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);

        if("String".equals(type)){
            return sp.getString(key, (String)defaultObject);
        }
        else if("Integer".equals(type)){
            return sp.getInt(key, (Integer)defaultObject);
        }
        else if("Boolean".equals(type)){
            return sp.getBoolean(key, (Boolean)defaultObject);
        }
        else if("Float".equals(type)){
            return sp.getFloat(key, (Float)defaultObject);
        }
        else if("Long".equals(type)){
            return sp.getLong(key, (Long)defaultObject);
        }

        return null;
    }
    public static boolean getParam(Context context , String key, boolean defaultObject){
        SharedPreferences sp = context.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key, defaultObject);
    }

    public static void remove(Context context ,String key){
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().remove(key).apply();
    }

    public static void clear(){
        SharedPreferences sp = mContext.getSharedPreferences(FILE_NAME, Context.MODE_PRIVATE);
        sp.edit().clear();
    }

    public static boolean isEmpty(Object o) {
        if (null == o)
            return true;

        if (o instanceof String)
            return "".equals(o) ? true : false;

        if (o instanceof List)
            return ((List) o).size() == 0 ? true : false;

        if (o instanceof Map)
            return ((Map) o).size() == 0 ? true : false;
        if (o instanceof String[])
            return ((String[]) o).length == 0 ? true : false;
        if (o instanceof int[])
            return ((int[]) o).length == 0 ? true : false;
        if (o instanceof Set)
            return ((Set) o).size() == 0 ? true : false;

        return false;
    }

}
