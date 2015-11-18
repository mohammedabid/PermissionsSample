package com.example.mohammadabidhussain.permissionssample;

import android.support.annotation.NonNull;

/**
 * Created by mohammadabid.hussain on 11/9/2015.
 */
public interface OnPermissionRequestListener {
    /**
     * Callback to be invoked when permission is granted
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onPermissionsGrantedResult(int requestCode, @NonNull String[] permissions,
                                    @NonNull int[] grantResults);
    /**
     * Callback to be invoked when permission is denied
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onPermissionsDeniedResult(int requestCode, @NonNull String[] permissions,
                                   @NonNull int[] grantResults);
    /**
     * Callback to be invoked when permission has denied before no permission dialog could be shown
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    void onPermissionsCancelledResult(int requestCode, @NonNull String[] permissions,
                                      @NonNull int[] grantResults);
}
