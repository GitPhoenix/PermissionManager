package com.alley.permission.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.Toast;

import com.alley.permission.R;
import com.alley.permission.base.BaseActivity;

public class MainActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.tvPermissionStorage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0003);
            }
        });

        findViewById(R.id.tvPermissionCamera).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestPermission(new String[]{Manifest.permission.CAMERA}, 0x0004);
            }
        });
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //这里只是一个测试
        requestPermission(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 0x0003);
    }

    @Override
    public void onPermissionGrant(int requestCode, @NonNull String[] permissions) {
        super.onPermissionGrant(requestCode, permissions);
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
    public void onPermissionDenied(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onPermissionDenied(requestCode, permissions, grantResults);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //如果在Fragment中进行权限请求操作的话，还需在主Activity中调用Fragment中的onRequestPermissionsResult()方法
    }
}
