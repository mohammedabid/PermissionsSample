package com.example.mohammadabidhussain.permissionssample;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSIONS_REQUEST_CODE = 7;
    private static final int PERMISSIONS_REQUEST_CODE_NOT_GRANTED = 8;
    private OnPermissionRequestListener onPermissionRequestListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public static boolean checkForPermission(Activity thisActivity, String permission) {
        int permissionCheck = ContextCompat.checkSelfPermission(thisActivity,
                permission);
        return permissionCheck == PackageManager.PERMISSION_GRANTED;
    }

    /**
     * @param permission                  string constant representing the permission, for simplicity, the utility method provides only 1 permission parameter at a time, otherwise you may request for multiple permissions at a time.
     * @param resIdExplanation            resId for the String resource used as the explanation to show to the user, which states why that particular permission is needed
     * @param onPermissionRequestListener the listener to be invoked whenever a permission is allowed or denied.
     */
    public void requestForPermission(final String permission, int resIdExplanation, OnPermissionRequestListener onPermissionRequestListener) {

        this.onPermissionRequestListener = onPermissionRequestListener;

        // Should we show an explanation?
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                permission)) {

            // Show an explanation to the user *asynchronously* -- don't block
            // this thread waiting for the user's response! After the user
            // sees the explanation, try again to request the permission.
            Utils.showAlertDialog(this, "Permission request", "This app needs access to your storage in order to attach the files, click Yes if you agree, or No if you don't want to attach the files.", 1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    ActivityCompat.requestPermissions(MainActivity.this,
                            new String[]{permission},
                            PERMISSIONS_REQUEST_CODE);
                }
            });
        } else {

            // No explanation needed, we can request the permission.
            if (Utils.getFromPrefs(this, "alreadyDenied", false)) {
                ActivityCompat.requestPermissions(this,
                        new String[]{permission},
                        PERMISSIONS_REQUEST_CODE_NOT_GRANTED);
            } else {

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{permission},
                        PERMISSIONS_REQUEST_CODE);
                Utils.saveToPrefs(this, "alreadyDenied", true);
            }
        }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {

        switch (requestCode) {

            case PERMISSIONS_REQUEST_CODE:
                if (permissions.length > 0) {
                    if (permissions[0].equalsIgnoreCase(android.Manifest.permission.READ_EXTERNAL_STORAGE)) {
                        if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                            // Camera permission has been granted, preview can be displayed
                            Log.i(getClass().getName(), "Permission has now been granted. Showing preview.");
                            Snackbar.make(getWindow().getDecorView(), "Permission granted",
                                    Snackbar.LENGTH_SHORT).show();
                            onPermissionRequestListener.onPermissionsGrantedResult(requestCode, permissions, grantResults);

                        } else {
                            Log.i(getClass().getName(), "Permission was NOT granted.");
                            Snackbar.make(getWindow().getDecorView(), "Permission was not granted",
                                    Snackbar.LENGTH_SHORT).show();
                            onPermissionRequestListener.onPermissionsDeniedResult(requestCode, permissions, grantResults);

                        }
                    }
                }
                break;

            case PERMISSIONS_REQUEST_CODE_NOT_GRANTED:
                if (permissions.length > 0) {
                    if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                        // Camera permission has been granted, preview can be displayed
                        Log.i(getClass().getName(), "Permission has now been granted. Showing preview.");
                        Snackbar.make(getWindow().getDecorView(), "Permission granted",
                                Snackbar.LENGTH_SHORT).show();
                        onPermissionRequestListener.onPermissionsGrantedResult(requestCode, permissions, grantResults);
                    } else {
                        Log.i(getClass().getName(), "Permission was NOT granted.");
                        this.onPermissionRequestListener.onPermissionsCancelledResult(requestCode, permissions, grantResults);
                        Utils.showAlertDialog(this, "Permission request", "You have denied the permission, do you want to open application settings to grant the permission?", 1, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                try {
                                    //Open the specific App Info page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                    intent.setData(Uri.parse("package:" + MainActivity.this.getPackageName()));
                                    startActivity(intent);

                                } catch (ActivityNotFoundException e) {
                                    //e.printStackTrace();
                                    //Open the generic Apps page:
                                    Intent intent = new Intent(android.provider.Settings.ACTION_MANAGE_APPLICATIONS_SETTINGS);
                                    startActivity(intent);

                                }
                            }
                        });
                    }

                }
                break;

            default:
                break;
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }
}
