package com.task.vasskob.googlemaps.screens.common.model.repository;

import android.util.Log;

import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerRealm;

import java.util.List;

import io.realm.Realm;

public class MarkerRealmRepository implements Repository<MarkerRealm> {

    private final Realm realm;
    private OnMarkerChangeClickListener listener;
    private RealmSpecification mSpecification;

    public MarkerRealmRepository() {
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void add(final MarkerRealm marker) {
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealm(marker);
            }
        });
    }

    @Override
    public void add(final List<MarkerRealm> list) {
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
    public void update(MarkerRealm marker) {
        final String markerId = marker.getId();
        final MarkerIcon markerIcon = marker.getMarkerIcon();
        final String markerTitle = marker.getTitle();
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                MarkerRealm changedMarker = realm.where(MarkerRealm.class).equalTo("id", markerId).findFirst();
                changedMarker.setTitle(markerTitle);
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

    @Override
    public void delete(MarkerRealm marker) {
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
    public List<MarkerRealm> query(Specification specification) {
        mSpecification = (RealmSpecification) specification;
        mSpecification.toRealmResults(realm);
        // return data via callback in Specification
        return null;
    }

    public void setListener(OnMarkerChangeClickListener listener) {
        this.listener = listener;
    }

    public void removeListener() {
        if (mSpecification != null) {
            mSpecification.removeListeners();
        }
    }
}
