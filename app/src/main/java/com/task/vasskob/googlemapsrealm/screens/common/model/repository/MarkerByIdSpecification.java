package com.task.vasskob.googlemapsrealm.screens.common.model.repository;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MarkerByIdSpecification implements RealmSpecification {
    private final String id;
    private RealmResults<Marker> resultsForId;
    private final OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback;

    public MarkerByIdSpecification(final String id, OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback) {
        this.id = id;
        this.callback = callback;
    }

    @Override
    public void toRealmResults(Realm realm) {
        resultsForId = realm.where(Marker.class).equalTo("id", id).findAllAsync();
        resultsForId.addChangeListener(callback);
    }

    @Override
    public void removeListeners() {
        if (resultsForId != null) {
            resultsForId.removeAllChangeListeners();
        }
    }
}
