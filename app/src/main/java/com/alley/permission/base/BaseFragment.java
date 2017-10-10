package com.alley.permission.base;


import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.View;

import com.alley.pm.PermissionManager;

public class BaseFragment extends Fragment implements PermissionManager.OnRequestPermissionListener {
    protected String TAG = this.getClass().getSimpleName();

    protected Activity activity;
    protected PermissionManager permissionManager;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (Activity) context;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        permissionManager = new PermissionManager(activity);
        permissionManager.setOnRequestPermissionListener(this);
    }

    //如果在Fragment中，进行权限请求操作的话，还需在主Activity中调用Fragment中的onRequestPermissionsResult()方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionManager.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onPermissionGrant(boolean isGranted, String[] permissions, int requestCode) {
        Log.i(TAG, "获取权限成功——>" + "isGranted：" + isGranted + " requestCode：" + requestCode);
    }

    @Override
    public void onPermissionDenied(boolean isDenied, String[] permissions, int requestCode) {
        Log.i(TAG, "获取权限失败——>" + "isGranted：" + isDenied + " requestCode：" + requestCode);
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
