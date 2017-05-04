package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import java.util.List;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController implements DbController {

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

    @Override
    public void addMarkerToRealm(final Marker marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
    }


    public void addMarkerListToRealm(final List<Marker> list) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < list.size(); i++) {
                    realm.copyToRealm(list.get(i));
                }
            }
        });
    }

    // TODO: 04.05.17 async delete & update
    @Override
    public void deleteMarkerFromRealm(final Marker marker) {
        realm.beginTransaction();
        marker.deleteFromRealm();
        realm.commitTransaction();
    }

    @Override
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

    public void showAllMarkers(OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener) {
        resultsForAll = realm.where(Marker.class).findAllAsync();
        resultsForAll.addChangeListener(listener);
    }

    public void showMarker(String id, OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener) {
        realmResults = realm.where(Marker.class).equalTo("id", id).findAllAsync();
        realmResults.addChangeListener(listener);
    }

    public void removeListener() {
        if (realmResults != null && resultsForAll != null) {
            resultsForAll.removeAllChangeListeners();
            realmResults.removeAllChangeListeners();
        }
    }
}