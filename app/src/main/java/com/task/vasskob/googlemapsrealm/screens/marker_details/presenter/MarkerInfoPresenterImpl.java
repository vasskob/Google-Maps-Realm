package com.task.vasskob.googlemapsrealm.screens.marker_details.presenter;

import android.util.Log;

import com.task.vasskob.googlemapsrealm.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;

import com.task.vasskob.googlemapsrealm.screens.marker_details.view.MarkerInfoView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView> {

    private MarkerInfoView mInfoView;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            Log.d(" OnChange", " marker.icon =" + collection.first().getMarkerIcon());
            mInfoView.showMarkerInfo(collection.get(0));
        }
    };

    @Override
    public void showMarkerInfoById(String id) {
        realmController.showMarker(id, callback);
    }

    @Override
    public void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon) {
        realmController.updateMarkerInRealm(marker, title, markerIcon);
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        realmController.deleteMarkerFromRealm(marker);
    }

    @Override
    public void setView(MarkerInfoView view) {
        mInfoView = view;
    }

    @Override
    public void clearView() {
        mInfoView = null;
    }

}
