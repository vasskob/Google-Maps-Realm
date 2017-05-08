package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import android.util.Log;

import com.task.vasskob.googlemapsrealm.listeners.db.OnMarkerChangeClickListener;
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
    private OnMarkerChangeClickListener listener;

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
    public void addMarkerToDb(final Marker marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
    }

    @Override
    public void addMarkerListToDb(final List<Marker> list) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < list.size(); i++) {
                    realm.copyToRealm(list.get(i));
                }
            }
        });
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        final String markerId = marker.getId();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.where(Marker.class).equalTo("id", markerId).findFirst().deleteFromRealm();
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("deleteMarkerInDb", "onError: ", error);
                listener.onError();
            }
        });
    }

    @Override
    public void updateMarkerInDb(final Marker marker, final String title, final MarkerIcon markerIcon) {
        final String markerId = marker.getId();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Marker changedMarker = realm.where(Marker.class).equalTo("id", markerId).findFirst();
                changedMarker.setTitle(title);
                if (markerIcon != null) {
                    MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
                    mIcon.setId(markerIcon.getId());
                    mIcon.setResId(markerIcon.getResId());
                    changedMarker.setMarkerIcon(mIcon);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                listener.onSuccess();
            }
        }, new Realm.Transaction.OnError() {
            @Override
            public void onError(Throwable error) {
                Log.e("updateMarkerInDb", "onError: ", error);
                listener.onError();
            }
        });
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

    public void setListener(OnMarkerChangeClickListener listener) {
        this.listener = listener;
    }


}