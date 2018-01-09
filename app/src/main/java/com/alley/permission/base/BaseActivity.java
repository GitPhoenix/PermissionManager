package com.alley.permission.base;


import android.content.DialogInterface;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.alley.permission.util.ActivityUtils;
import com.alley.pm.PermissionManager;

public class BaseActivity extends AppCompatActivity implements PermissionManager.OnRequestPermissionListener {
    protected String TAG = this.getClass().getSimpleName();

    private PermissionManager permissionManager;

    /**
     * 动态权限申请
     *
     * @param permissions
     * @param requestCode
     */
    public void requestPermission(@NonNull String[] permissions, @IntRange(from = 0) int requestCode) {
        permissionManager = new PermissionManager(this);
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
        new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage(hint)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ActivityUtils.startSystemSettings(BaseActivity.this);
                    }
                })
                .setNegativeButton("取消", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).show();
    }
}
