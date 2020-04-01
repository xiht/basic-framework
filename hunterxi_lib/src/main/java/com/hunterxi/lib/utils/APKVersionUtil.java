package com.hunterxi.lib.utils;

import android.content.Context;
import android.content.pm.PackageManager;

/**
 * @author HunterXi
 * 创建日期：2019/4/23 15:49
 * 描述：应用版本信息工具类
 */
public class APKVersionUtil {

    /**
     * 获取应用程序名称
     * @param mContext
     * @return
     */
    public static String getAppName(Context mContext) {
        String appName = "";
        try {
            int labelRes = mContext.getPackageManager()
                    .getPackageInfo(mContext.getPackageName(), 0)
                    .applicationInfo.labelRes;
            appName = mContext.getResources().getString(labelRes);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return appName;
    }

    /**
     * 获取当前本地apk的版本号
     * 一般用于本地app和后台的app提供的版本进行对比，实现更新功能
     * @param mContext
     * @return
     */
    public static int getVersionCode(Context mContext) {
        int versionCode = 0;
        try {
            //获取软件版本号，对应AndroidManifest.xml下android:versionCode
            versionCode = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return versionCode;
    }

    /**
     * 获取版本号名称
     * 一般用于展示给用户看的版本信息
     * @param mContext
     * @return
     */
    public static String getVersionName(Context mContext) {
        String versionName = "";
        try {
            versionName = mContext.getPackageManager().
                    getPackageInfo(mContext.getPackageName(), 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
