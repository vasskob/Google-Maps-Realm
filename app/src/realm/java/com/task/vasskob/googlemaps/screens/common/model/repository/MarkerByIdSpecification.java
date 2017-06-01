package com.task.vasskob.googlemaps.screens.common.model.repository;

import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerRealm;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MarkerByIdSpecification implements RealmSpecification {
    private final String id;
    private RealmResults<MarkerRealm> resultsForId;
    private final OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback;

    public MarkerByIdSpecification(final String id, OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback) {
        this.id = id;
        this.callback = callback;
    }

    @Override
    public void toRealmResults(Realm realm) {
        resultsForId = realm.where(MarkerRealm.class).equalTo("id", id).findAllAsync();
        resultsForId.addChangeListener(callback);
    }

    @Override
    public void removeListeners() {
        if (resultsForId != null) {
            resultsForId.removeAllChangeListeners();
        }
    }
}
