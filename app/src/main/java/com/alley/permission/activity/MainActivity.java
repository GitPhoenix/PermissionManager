package com.alley.permission.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.alley.permission.base.BaseActivity;
import com.alley.permission.R;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tvPermissionStorage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionManager.requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0003);
            }
        });

        findViewById(R.id.tvPermissionCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                permissionManager.requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0004);
            }
        });
    }

    @Override
    public void onPermissionGrant(boolean isGranted, String[] permissions, int requestCode) {
        super.onPermissionGrant(isGranted, permissions, requestCode);
        switch (requestCode) {
            case 0x0003:
                Toast.makeText(this, "请求存储权限成功", Toast.LENGTH_SHORT).show();
                break;

            case 0x0004:
                Toast.makeText(this, "请求拍照权限成功", Toast.LENGTH_SHORT).show();
                break;

            default:
                break;
        }
    }

    @Override
    public void onPermissionDenied(boolean isGranted, String[] permissions, int requestCode) {
        super.onPermissionDenied(isGranted, permissions, requestCode);
        switch (requestCode) {
            case 0x0003:
                showDialogForPermission("在设置-应用-微信-权限中开启存储空间权限，以正常使用微信功能");
                break;

            case 0x0004:
                showDialogForPermission("在设置-应用-微信-权限中开启相机权限，以正常使用微信功能");
                break;

            default:
                break;
        }
    }

    //如果在Fragment中，进行权限请求操作的话，还需在主Activity中调用Fragment中的onRequestPermissionsResult()方法
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

    }
}
