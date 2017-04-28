package com.task.vasskob.googlemapsrealm.model.realm;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private static RealmController instance;
    private final Realm realm;

    private RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public void refresh() {
        realm.refresh();
    }

    public void addMarkerToRealm(Marker marker) {
        realm.beginTransaction();
        realm.copyToRealm(marker);
        realm.commitTransaction();
    }

    public void deleteMarkerInRealm(Marker object) {
        realm.beginTransaction();
        object.removeFromRealm();
        realm.commitTransaction();
    }

    public void updateMarkerInRealm(Marker marker, String title, MarkerIcon markerIcon) {
        realm.beginTransaction();
        marker.setTitle(title);
        if (markerIcon != null) {
            MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
            mIcon.setId(markerIcon.getId());
            mIcon.setResId(markerIcon.getResId());
            marker.setMarkerIcon(mIcon);
        }
        realm.commitTransaction();
    }

    public void clearAll() {
        realm.beginTransaction();
        realm.clear(Marker.class);
        realm.commitTransaction();
    }

    public RealmResults<Marker> getMarkers() {
        return realm.where(Marker.class).findAll();
    }

    public Marker getMarker(long id) {
        return realm.where(Marker.class).equalTo("id", id).findFirst();
    }

    public void closeRealm() {
        realm.close();
    }

    public boolean hasMarkers() {
        return !realm.allObjects(Marker.class).isEmpty();
    }

    //query example
    public RealmResults<Marker> queryedMarkers() {
        return realm.where(Marker.class)
                .contains("mLabel", "a")
                .or()
                .contains("mIcon", "icon_path")
                .findAllAsync();

    }
}