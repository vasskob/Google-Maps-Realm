package com.task.vasskob.googlemapsrealm.screens.common.model.db;

import android.util.Log;

import com.task.vasskob.googlemapsrealm.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemapsrealm.screens.common.model.mapper.MarkerToMarkerRealmMaper;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import java.util.List;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

class RealmController implements DbController {

    private static RealmController instance;
    private final Realm realm;
    private RealmResults<MarkerRealm> resultsForAll;
    private RealmResults<MarkerRealm> realmResults;
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
                realm.copyToRealm(new MarkerToMarkerRealmMaper().map(marker));
            }
        });
    }

    @Override
    public void addMarkerListToDb(final List<Marker> list) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < list.size(); i++) {
                    MarkerRealm markerRealm = new MarkerToMarkerRealmMaper().map(list.get(i));
                    realm.copyToRealm(markerRealm);
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
                realm.where(MarkerRealm.class).equalTo("id", markerId).findFirst().deleteFromRealm();
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
                MarkerRealm changedMarker = realm.where(MarkerRealm.class).equalTo("id", markerId).findFirst();
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

    public void showAllMarkers(OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> listener) {
        resultsForAll = realm.where(MarkerRealm.class).findAllAsync();
        resultsForAll.addChangeListener(listener);
    }

    public void showMarker(String id, OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> listener) {
        realmResults = realm.where(MarkerRealm.class).equalTo("id", id).findAllAsync();
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