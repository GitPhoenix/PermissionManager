package com.alley.permission.util;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.ActivityNotFoundException;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.RequiresPermission;

import java.util.List;

/**
 * Activity跳转工具类
 *
 * @author Phoenix
 * @date 2016-6-7 16:14
 */
public final class ActivityUtils {

    private ActivityUtils() {}

    /**
     * 从一个activity 跳转到另一个activity，此时目标activity将会存在于启动他的activity所在的栈中
     *
     * @param activity activity的上下文信息
     * @param cls      目标activity.class
     */
    public static void start(Activity activity, Class<?> cls) {
        Intent intent = new Intent(activity, cls);
        activity.startActivity(intent);
    }

    /**
     * 当上下文信息是应用全局Context时启动activity， 此时目标activity将会在一个新的任务栈中, 内部设置了Flag:Intent.FLAG_ACTIVITY_NEW_TASK
     *
     * @param context 全局上下文
     * @param cls
     */
    public static void startNew(Context context, Class<?> cls) {
        Intent intent = new Intent(context, cls);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 挈带参数启动Activity
     *
     * @param activity 上下文
     * @param cls      目标class文件
     * @param bundle   内容bundle
     */
    public static void start(Activity activity, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(activity, cls);
        intent.putExtras(bundle);
        activity.startActivity(intent);
    }

    /**
     * 挈带参数启动Activity
     *
     * @param context 上下文
     * @param cls     目标class文件
     * @param bundle  内容bundle
     */
    public static void startNew(Context context, Class<?> cls, Bundle bundle) {
        Intent intent = new Intent();
        intent.setClass(context, cls);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(intent);
    }

    /**
     * 回到系统桌面
     *
     * @param activity
     */
    public static void startSystemHome(Activity activity) {
        Intent homeIntent = new Intent(Intent.ACTION_MAIN);
        homeIntent.addCategory(Intent.CATEGORY_HOME);
        activity.startActivity(homeIntent);
    }

    /**
     * 启动当前应用系统设置页面
     *
     * @param activity
     */
    public static void startSystemSettings(Activity activity) {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        activity.startActivity(intent);
    }

    /**
     * 打开手机WIFI设置界面
     *
     * @param context
     */
    public static void startWifiSettings(Context context) {
        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        context.startActivity(intent);
    }

    /**
     * 携带电话号码跳转到拨号界面
     *
     * @param context
     * @param phoneNumber
     */
    public static void startSystemDial(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 直接拨打电话
     *
     * @param context
     * @param phoneNumber
     */
    @RequiresPermission(android.Manifest.permission.CALL_PHONE)
    public static void startSystemCall(Context context, String phoneNumber) {
        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phoneNumber));
        context.startActivity(intent);
    }

    /**
     * 打开指定包名的APP
     *
     * @param context
     * @param packageName
     */
    public static void startPackage(Context context, String packageName) {
        PackageManager manager = context.getPackageManager();
        Intent intentForPackage = null;
        try {
            intentForPackage = manager.getLaunchIntentForPackage(packageName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (intentForPackage != null) {
            context.startActivity(intentForPackage);
        }
    }

    /**
     * 使指定包名的应用从后台回到前台
     *
     * @param context
     * @param packageName
     */
    public static void startAppFromBackground(Context context, String packageName) {
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = manager.getRunningTasks(Integer.MAX_VALUE);

        if (tasks == null || tasks.isEmpty()) {
            return;
        }

        for (int i = 0; i < tasks.size(); i++) {
            ComponentName topActivity = tasks.get(i).topActivity;
            if (!packageName.equals(topActivity.getPackageName())) {
                continue;
            }
            Intent intent = new Intent();
            //这里是指从后台返回到前台，前两个是关键
            intent.setAction(Intent.ACTION_MAIN);
            intent.addCategory(Intent.CATEGORY_LAUNCHER);
            try {
                Class<?> cls = Class.forName(topActivity.getClassName());
                ComponentName componentName = new ComponentName(context, cls);
                intent.setComponent(componentName);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT | Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
                context.startActivity(intent);
                break;
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 跳转到手机应用市场：https://www.jianshu.com/p/2165acb5d1eb
     * 此方法支持应用宝、360手机助手、豌豆荚、小米商店等主流的应用市场
     *
     * @param context
     * @param packageName
     */
    public static void startMarket(Context context, String packageName) {
        Uri uri = Uri.parse("market://details?id=" + packageName);
        Intent intentMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            context.startActivity(intentMarket);
        } catch (ActivityNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * 不携带数据发送广播
     *
     * @param context
     * @param action
     */
    public static void sendBroadCast(Context context, String action) {
        Intent intent = new Intent();
        intent.setAction(action);
        context.sendBroadcast(intent);
    }
}
