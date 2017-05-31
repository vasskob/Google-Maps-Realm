package com.task.vasskob.googlemapsrealm.screens.common.model.repository;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class AllMarkersSpecification implements RealmSpecification {

    private final OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback;
    private RealmResults<Marker> resultsForAll;

    public AllMarkersSpecification(OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback) {
        this.callback = callback;
    }

    @Override
    public void toRealmResults(Realm realm) {
        resultsForAll = realm.where(Marker.class).findAllAsync();
        resultsForAll.addChangeListener(callback);
    }

    @Override
    public void removeListeners() {
        if (resultsForAll != null) {
            resultsForAll.removeAllChangeListeners();
        }
    }
}
