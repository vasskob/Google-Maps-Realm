package com.task.vasskob.googlemapsrealm.model.realm;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class RealmController {

    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener;

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
//   realm.refresh();
//   realm.setAutoRefresh(true);

        //  realm.waitForChange();
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
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                marker.deleteFromRealm();
            }
        });
    }

    public void updateMarkerInRealm(final Marker marker, final String title, final MarkerIcon markerIcon) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                marker.setTitle(title);
                if (markerIcon != null) {
                    MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
                    mIcon.setId(markerIcon.getId());
                    mIcon.setResId(markerIcon.getResId());
                    marker.setMarkerIcon(mIcon);
                }
            }
        });
    }

    public void getAllMarkers() {
        RealmResults<Marker> results = realm.where(Marker.class).findAllAsync();
        results.addChangeListener(listener);
    }

    public void getMarker(String id) {
        RealmResults<Marker> realmResults = realm.where(Marker.class).equalTo("id", id).findAllAsync();
        realmResults.addChangeListener(listener);
    }

    public void closeRealm() {
        // realm.close();
    }

    public void setListener(OrderedRealmCollectionChangeListener<RealmResults<Marker>> listener) {
        this.listener = listener;
    }

}