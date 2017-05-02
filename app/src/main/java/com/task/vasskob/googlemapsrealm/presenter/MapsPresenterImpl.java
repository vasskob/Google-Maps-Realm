package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.view.MapsView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private MapsView mMapsView;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            mMapsView.showMarkers(collection);
        }
    };

    @Override
    public void showMarkersOnMap() {
        realmController.setAllMarkersListener(callback);
        realmController.showAllMarkers();
    }

   @Override
    public void addMarkerToRealm(Marker marker) {
        realmController.addMarkerToRealm(marker);
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
