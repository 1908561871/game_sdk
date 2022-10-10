package com.zhibo8.game.sdk.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * author : Jeeson
 * date : 2019年12月09日 18:01
 * email : jisheng@zhibo8.cc
 * description : 工具类
 */
public class AppUtils {

    /**
     * 获取应用程序名称
     */

    public static synchronized String getAppName(Context context) {

        try {
            PackageManager packageManager = context.getPackageManager();
            PackageInfo packageInfo = packageManager.getPackageInfo(
                    context.getPackageName(), 0);
            int labelRes = packageInfo.applicationInfo.labelRes;
            return context.getResources().getString(labelRes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean isInstall(Context context, String packageName) {
        try {
            // 通包管理器，检索所有的应用程序（甚至卸载的）与数据目录
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(packageName, 0);
            return packageInfo != null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }
}
