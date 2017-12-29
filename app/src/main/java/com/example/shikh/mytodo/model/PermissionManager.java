package com.example.shikh.mytodo.model;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.shikh.mytodo.MainActivity;

import java.util.ArrayList;

/**
 * Created by shikh on 29-12-2017.
 */

public class PermissionManager {
    Activity activity;
    ArrayList<OnPermissionResult> permissionHandler = new ArrayList<>();
    public static final String  TAG = "PM";
    public PermissionManager(Activity activity) {
        this.activity = activity;
    }

    public void doWithPermission(String[] permissions , OnPermissionResult opr){
        Log.d(TAG, "Entered into dowithpermission tag");
        int deniedPermissions = 0;
        for(String permission : permissions){
            int perm = ContextCompat.checkSelfPermission(activity , permission);
            if(perm == PackageManager.PERMISSION_GRANTED){
                opr.onGranted(permission);
            }else{
                deniedPermissions++;
            }
            if(deniedPermissions>0){
                int requestCode = permissionHandler.size();
                permissionHandler.add(requestCode,opr);  /*at int index = requestCode we added the obj opr*/
                ActivityCompat.requestPermissions(
                        activity,
                        permissions,
                        requestCode
                );
            }
        }
    }

    public void onRequestPermissionResult(int requestCode,
                                          String[] permissions,
                                          int[]grantResult){
        for(int i=0;i<permissions.length;++i){
            if(grantResult[i] == PackageManager.PERMISSION_GRANTED){
                permissionHandler.get(requestCode).onGranted(permissions[i]);
            }else{
                permissionHandler.get(requestCode).onDenied(permissions[i]);
            }
        }
        permissionHandler.remove(requestCode);
    }

    public interface OnPermissionResult{
        void onGranted(String permission);
        void onDenied(String permission);
    }
}
