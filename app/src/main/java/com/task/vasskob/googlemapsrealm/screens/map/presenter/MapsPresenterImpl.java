package com.task.vasskob.googlemapsrealm.screens.map.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import com.task.vasskob.googlemapsrealm.screens.map.view.MapsView;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private MapsView mMapsView;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            mMapsView.showMarkersOnMap(collection);
        }
    };

    @Override
    public void showMarkersOnMap() {
        realmController.showAllMarkers(callback);
    }

   @Override
    public void addMarkerToRealm(Marker marker) {
        realmController.addMarkerToRealm(marker);
    }


    public void addMarkerListToRealm(List<Marker> markers) {
        realmController.addMarkerListToRealm(markers);
    }

    @Override
    public void removeListener() {
        realmController.removeListener();
    }

    @Override
    public void setView(MapsView view) {
        mMapsView = view;
    }

    @Override
    public void clearView() {
        mMapsView = null;
    }
}
