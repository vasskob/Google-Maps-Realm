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

    public RealmResults<Marker> getAllMarkers() {
        return realm.allObjects(Marker.class);
        // return realm.where(Marker.class).findAll();
    }

    public Marker getMarker(long id) {
        return realm.where(Marker.class).equalTo("id", id).findFirst();
    }

    public void closeRealm() {
        realm.close();
    }

    public void addMarkerToRealmAsync(final Marker marker, final OnTransactionCallback callback) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        }, new Realm.Transaction.Callback() {
            @Override
            public void onSuccess() {
                if (callback != null) {
                    callback.onRealmSuccess();
                }
            }
            @Override
            public void onError(Exception e) {

                if (callback != null) {
                    callback.onRealmError(e);
                }
            }
        });
    }

    public interface OnTransactionCallback {
        void onRealmSuccess();
        void onRealmError(final Exception e);
    }

//    public boolean hasMarkers() {
//        return !realm.allObjects(Marker.class).isEmpty();
//    }
//
//    //query example
//    public RealmResults<Marker> queryedMarkers() {
//        return realm.where(Marker.class)
//                .contains("mLabel", "a")
//                .or()
//                .contains("mIcon", "icon_path")
//                .findAllAsync();
//
//    }
//
//    public void clearAll() {
//        realm.beginTransaction();
//        realm.clear(Marker.class);
//        realm.commitTransaction();
//    }
}