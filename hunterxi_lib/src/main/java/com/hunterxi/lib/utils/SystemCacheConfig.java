package com.hunterxi.lib.utils;

import android.content.Context;

import com.orhanobut.logger.Logger;

/**
 * @author HunterXi
 * 创建日期：2019/5/9 10:17
 * 描述：系统缓存数据管理
 */
public class SystemCacheConfig {

    /**
     * 是否第一次启动应用程序
     */
    public static class HasFirstLaunchApp {

        public static final String KEY = "HAS_FIRST_LAUNCHER_APP";

        // 首次启动时在本地缓存文件中保存该值，以后启动读取到该值表明非首次启动
        public static final String STANDARD_VALUE = "is_not_the_first_time";

        private static String fileName = "first_launch_cache";

        public static String getSPLaunchFileName(Context context) {
            String appName = APKVersionUtil.getAppName(context);
            return appName + "_" + fileName;
        }

        public static String getLaunchKey(Context context, String key) {
            return (String) SharedPreferencesUtil.get(getSPLaunchFileName(context), context, key, "");
        }

        public static void setLaunchKey(Context context, String key, String value) {
            SharedPreferencesUtil.put(getSPLaunchFileName(context), context, key, value);
        }
    }
}
