package com.task.vasskob.googlemapsrealm.realm;

import android.app.Application;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.Realm;

import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.manageReverseMarkerIcon;


public class DbOperations {


    private static Realm realm;

    public DbOperations(Application application) {
        realm = RealmController.with(application).getRealm();
    }

    public static void writeMarkerToRealm(Marker marker) {
        realm.beginTransaction();
        realm.copyToRealm(marker);
        realm.commitTransaction();
    }

    public static void deleteMarkerInRealm(Marker object) {
        realm.beginTransaction();
        object.removeFromRealm();
        realm.commitTransaction();
    }

    public static void updateMarkerInRealm(Marker marker, String label, int iconId) {
        realm.beginTransaction();
        marker.setLabel(label);
        if (iconId != 0) {
            marker.setMarkerIcon(new MarkerIcon(iconId,iconId));
        }
        realm.commitTransaction();
    }

}
