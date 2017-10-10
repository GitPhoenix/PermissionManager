package com.alley.permission.base;


import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alley.pm.PermissionManager;

public class BaseActivity extends AppCompatActivity implements PermissionManager.OnRequestPermissionListener {
    protected String TAG = this.getClass().getSimpleName();

    protected PermissionManager permissionManager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        permissionManager = new PermissionManager(this);
        permissionManager.setOnRequestPermissionListener(this);
    }

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
    public void onPermissionDenied(boolean isGranted, String[] permissions, int requestCode) {
        Log.i(TAG, "获取权限失败——>" + "isGranted：" + isGranted + " requestCode：" + requestCode);
    }

    /**
     * 提示开启权限对话框，点击确定跳转到系统权限设置界面，可以根据需求自定义
     *
     * @param hint
     */
    public void showDialogForPermission(String hint) {
        new AlertDialog.Builder(this)
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
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivity(intent);
    }
}
