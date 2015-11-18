package com.example.mohammadabidhussain.permissionssample;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;

/**
 * Created by mohammadabid.hussain on 11/16/2015.
 */
public class Utils {

    public static void showAlertDialog(Activity thiz, String titleRes,
                                String msgRes, int btnRes, DialogInterface.OnClickListener onOkListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(thiz);
        builder.setTitle(titleRes)
                .setMessage(msgRes)
                .setCancelable(false);
        builder.setView(null);
        if (btnRes == 0) {
            btnRes = R.string.lbl_ok;
        }

        if(btnRes == 1){
            btnRes = R.string.lbl_yes;
            builder.setNegativeButton(
                    R.string.lbl_no,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    });
        }

        builder.setPositiveButton(
                btnRes,
                onOkListener);

        AlertDialog alert = builder.create();
        alert.show();
    }

    public static void saveToPrefs(Context context, String key, boolean value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);
        final SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, value);
        editor.commit();
    }


    public static boolean getFromPrefs(Context context, String key, boolean defaultValue) {
        SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(context);
        try {
            return sharedPrefs.getBoolean(key, defaultValue);
        } catch (Exception e) {
            e.printStackTrace();
            return defaultValue;
        }
    }


}
