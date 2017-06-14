package com.task.vasskob.googlemaps.screens.common.model.repository;

import android.util.Log;

import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerToMarkerRealmMaper;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.List;

import io.realm.Realm;

public class MarkerRepository implements Repository<Marker> {

    private final Realm realm;
    private OnMarkerChangeClickListener listener;
    private RealmSpecification mSpecification;

    public MarkerRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void add(Marker marker) {
        final MarkerRealm markerRealm = new MarkerToMarkerRealmMaper().map(marker);
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(markerRealm);
            }
        });
    }

    @Override
    public void add(final List<Marker> list) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                for (int i = 0; i < list.size(); i++) {
                    realm.copyToRealm(new MarkerToMarkerRealmMaper().map(list.get(i)));
                }
            }
        });
    }

    @Override
    public void update(Marker marker) {
        final String markerId = marker.getId();
        final MarkerIcon markerIcon = marker.getMarkerIcon();
        final String markerTitle = marker.getTitle();
        final int id = markerIcon.getId();
        final int resId = markerIcon.getResId();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MarkerRealm changedMarker = realm.where(MarkerRealm.class).equalTo("id", markerId).findFirst();
                changedMarker.setTitle(markerTitle);
                if (markerIcon != null) {
                    MarkerIcon mIcon = realm.createObject(MarkerIcon.class);
                    mIcon.setId(id);
                    mIcon.setResId(resId);
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

    @Override
    public void delete(Marker marker) {
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
    public List<Marker> query(Specification specification) {
        mSpecification = (RealmSpecification) specification;
        mSpecification.toRealmResults(realm);
        // return data via callback in Specification
        return null;
    }

    @Override
    public void setResultListener(OnMarkerChangeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener() {
        if (mSpecification != null) {
            mSpecification.removeListeners();
        }
    }
}
