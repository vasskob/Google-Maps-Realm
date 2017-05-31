package com.task.vasskob.googlemapsrealm.screens.common.model.repository;

import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerRealm;

import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class AllMarkersSpecification implements RealmSpecification {

    private final OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback;
    private RealmResults<MarkerRealm> resultsForAll;

    public AllMarkersSpecification(OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback) {
        this.callback = callback;
    }

    @Override
    public void toRealmResults(Realm realm) {
        resultsForAll = realm.where(MarkerRealm.class).findAllAsync();
        resultsForAll.addChangeListener(callback);
    }

    @Override
    public void removeListeners() {
        if (resultsForAll != null) {
            resultsForAll.removeAllChangeListeners();
        }
    }
}
