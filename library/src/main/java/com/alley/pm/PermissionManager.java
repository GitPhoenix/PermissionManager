package com.alley.pm;


import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

/**
 * Application for dynamic permission
 *
 * @author Phoenix
 * @date 2017/10/10 13:42
 */
public class PermissionManager {
    private int REQUEST_CODE_ASK_PERMISSION = 0x00099;

    private Activity activity;
    private OnRequestPermissionListener permissionListener;

    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    /**
     * Requests permissions to be granted to this application. These permissions
     * must be requested in your manifest.
     *
     * @param permissions The requested permissions.
     * @param requestCode The request code.
     */
    public void requestPermission(@NonNull String[] permissions, @IntRange(from = 0) int requestCode) {
        this.REQUEST_CODE_ASK_PERMISSION = requestCode;
        if (permissionListener == null) {
            return;
        }

        if (checkPermissions(permissions)) {
            permissionListener.onPermissionGrant(true, permissions, REQUEST_CODE_ASK_PERMISSION);
        } else {
            List<String> needPermissions = getDeniedPermissions(permissions);
            ActivityCompat.requestPermissions(activity, needPermissions.toArray(new String[needPermissions.size()]), REQUEST_CODE_ASK_PERMISSION);
        }
    }

    /**
     * Determine whether have been granted a particular permission.
     *
     * @param permissions
     * @return
     */
    private boolean checkPermissions(String[] permissions) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
            return true;
        }

        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * The name of the permission being checked.
     *
     * @param permissions
     * @return
     */
    private List<String> getDeniedPermissions(String[] permissions) {
        List<String> needRequestPermissionList = new ArrayList<>();
        for (String permission : permissions) {
            if (ContextCompat.checkSelfPermission(activity, permission) != PackageManager.PERMISSION_GRANTED || ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
                needRequestPermissionList.add(permission);
            }
        }
        return needRequestPermissionList;
    }

    /**
     * Verify the grant results for the corresponding permissions.
     *
     * @param grantResults
     * @return
     */
    private boolean verifyPermissions(int[] grantResults) {
        for (int grantResult : grantResults) {
            if (grantResult != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    /**
     * Callback for the result from requesting permissions.
     *
     * @param requestCode  The request code.
     * @param permissions  The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions.
     */
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (permissionListener == null) {
            return;
        }

        if (requestCode == REQUEST_CODE_ASK_PERMISSION) {
            if (verifyPermissions(grantResults)) {
                permissionListener.onPermissionGrant(true, permissions, REQUEST_CODE_ASK_PERMISSION);
            } else {
                permissionListener.onPermissionDenied(false, permissions, REQUEST_CODE_ASK_PERMISSION);
            }
        }
    }

    public interface OnRequestPermissionListener {
        /**
         * The permissions was granted.
         *
         * @param isGranted
         * @param permissions The requested permissions.
         * @param requestCode The request code.
         */
        void onPermissionGrant(boolean isGranted, @NonNull String[] permissions, int requestCode);

        /**
         * The permissions was denied.
         *
         * @param isDenied
         * @param permissions The requested permissions.
         * @param requestCode The request code.
         */
        void onPermissionDenied(boolean isDenied, @NonNull String[] permissions, int requestCode);
    }

    /**
     * Register listener.
     *
     * @param permissionListener
     */
    public void setOnRequestPermissionListener(OnRequestPermissionListener permissionListener) {
        this.permissionListener = permissionListener;
    }
}
