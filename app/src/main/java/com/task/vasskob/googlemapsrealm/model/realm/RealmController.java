package com.task.vasskob.googlemapsrealm.model.realm;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

// TODO: 03/05/17 model interface and model implementation should be declared
public class RealmController {

    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> allMarkersListener;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> markerListener;

    private static RealmController instance;
    private final Realm realm;
    private RealmResults<Marker> resultsForAll;
    private RealmResults<Marker> realmResults;

    private RealmController() {
        realm = Realm.getDefaultInstance();
    }

    public static RealmController getInstance() {
        if (instance == null) {
            instance = new RealmController();
        }
        return instance;
    }

    public void addMarkerToRealm(final Marker marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
    }

    public void deleteMarkerInRealm(final Marker marker) {
        realm.beginTransaction();
        marker.deleteFromRealm();
        realm.commitTransaction();
    }

    public void updateMarkerInRealm(final Marker marker, final String title, final MarkerIcon markerIcon) {
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

    public void showAllMarkers() {
        resultsForAll = realm.where(Marker.class).findAllAsync();
        resultsForAll.addChangeListener(allMarkersListener);
    }

    public void showMarker(String id) {
        realmResults = realm.where(Marker.class).equalTo("id", id).findAllAsync();
        realmResults.addChangeListener(markerListener);
    }

    public void setAllMarkersListener(OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener) {
        this.allMarkersListener = listener;
    }

    public void setMarkerListener(OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener) {
        this.markerListener = listener;
    }

    public void removeListener() {
        if (realmResults != null && resultsForAll != null) {
            resultsForAll.removeAllChangeListeners();
            realmResults.removeAllChangeListeners();
        }
    }
}