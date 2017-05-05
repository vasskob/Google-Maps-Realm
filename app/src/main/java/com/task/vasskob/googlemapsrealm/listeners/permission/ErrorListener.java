package com.task.vasskob.googlemapsrealm.listeners.permission;

import android.util.Log;

import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequestErrorListener;

public class ErrorListener implements PermissionRequestErrorListener {
    @Override public void onError(DexterError error) {
        Log.e("ErrorListener", " onError : " + error.toString());

    }
}