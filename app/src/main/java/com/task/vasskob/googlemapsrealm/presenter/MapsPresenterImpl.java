package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.view.MapsView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private MapsView mMapsView;

    @Override
    public void showMarkersOnMap() {
        //RealmResults<Marker> markers =
        //    realmController.setOnChangeListener(this);
        realmController.setListener(callback);
        realmController.getAllMarkers();
        //   markers.load();
        // mMapsView.showMarkers(markers);
    }

    @Override
    public void updateRealm() {
        realmController.refresh();
    }

    @Override
    public void addMarkerToRealm(Marker marker) {
        realmController.addMarkerToRealm(marker);
    }

    @Override
    public void closeRealm() {
        realmController.closeRealm();
    }

    @Override
    public int getMarkersAmount() {
        //  RealmResults<Marker> results =
        realmController.setListener(callback);
        realmController.getAllMarkers();

        return 40;
        //results.size();
    }

    @Override
    public void setView(MapsView view) {
        mMapsView = view;
    }

    @Override
    public void clearView() {
        mMapsView = null;
    }


private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
    @Override
    public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
             mMapsView.showMarkers(collection);
    }
};



}
