package com.task.vasskob.googlemaps.listeners.permission;

import android.widget.Toast;

import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.screens.map.view.MapsActivity;

import java.util.List;

public class MultiplePermissionListener implements MultiplePermissionsListener {

    private final MapsActivity activity;

    public MultiplePermissionListener(MapsActivity activity) {
        this.activity = activity;
    }

    @Override
    public void onPermissionsChecked(MultiplePermissionsReport report) {
        for (PermissionGrantedResponse response : report.getGrantedPermissionResponses()) {
            Toast.makeText(activity, R.string.permission_granted, Toast.LENGTH_LONG).show();
            activity.setCurrentLocation();
        }
    }

    @Override
    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions,
                                                   PermissionToken token) {

    }

}
