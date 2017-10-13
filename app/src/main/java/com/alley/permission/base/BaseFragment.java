package com.alley.permission.base;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.provider.Settings;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;

import com.alley.pm.PermissionManager;

public class BaseFragment extends Fragment implements PermissionManager.OnRequestPermissionListener {
    protected String TAG = this.getClass().getSimpleName();

    protected Activity activity;
    private PermissionManager permissionManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    /**
     * 动态权限申请
     *
     * @param permissions
     * @param requestCode
     */
    public void requestPermission(@NonNull String[] permissions, @IntRange(from = 0) int requestCode) {
        permissionManager = new PermissionManager(activity);
        permissionManager.setOnRequestPermissionListener(this);
        permissionManager.requestPermission(permissions, requestCode);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGrant(int requestCode, @NonNull String[] permissions) {
        Log.i(TAG, "获取权限成功——>requestCode：" + requestCode);
    }

    @Override
    public void onPermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.i(TAG, "获取权限失败——>requestCode：" + requestCode);
    }

    /**
     * 提示开启权限对话框，点击确定跳转到系统权限设置界面，可以根据需求自定义
     *
     * @param hint
     */
    public void showDialogForPermission(String hint) {
        new AlertDialog.Builder(activity)
                .setTitle("权限申请")
                .setMessage(hint)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        startAppSettings();
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    /**
     * 启动当前应用系统设置页面
     */
    public void startAppSettings() {
        Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
        intent.setData(Uri.parse("package:" + activity.getPackageName()));
        startActivity(intent);
    }
}
