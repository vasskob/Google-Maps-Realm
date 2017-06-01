package com.task.vasskob.googlemaps.screens.common.model.repository;

import com.task.vasskob.googlemaps.listeners.db.OnDataLoadedListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerRealmToMarkerMapper;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.Realm;
import io.realm.RealmResults;

public class MarkerByIdSpecification implements RealmSpecification {
    private final String id;
    private RealmResults<MarkerRealm> resultsForId;

    private OnDataLoadedListener listener;
    private List<Marker> markers = new ArrayList<>();
    private OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback = new OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>>() {
        @Override
        public void onChange(RealmResults<MarkerRealm> collection, OrderedCollectionChangeSet changeSet) {
            markers.clear();
            for (MarkerRealm markerRealm : collection) {
                markers.add(new MarkerRealmToMarkerMapper().map(markerRealm));
            }
            listener.onDateReady(markers);
        }
    };

    public MarkerByIdSpecification(final String id, OnDataLoadedListener listener) {
        this.id = id;
        this.listener = listener;
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
