package com.task.vasskob.googlemapsrealm.presenter;

import android.util.Log;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.view.MarkerInfoView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView> {

    private MarkerInfoView mInfoView;

    @Override
    public void showMarkerInfoById(String id) {
        realmController.setListener(callback);
        realmController.getMarker(id);
    }

    @Override
    public void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon) {
        realmController.updateMarkerInRealm(marker, title, markerIcon);
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        realmController.deleteMarkerInRealm(marker);
    }

    @Override
    public void setView(MarkerInfoView view) {
        mInfoView = view;
    }

    @Override
    public void clearView() {
        mInfoView = null;
    }

    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            Log.d(" OnChange" ," marker.icon ="+ collection.first().getMarkerIcon() );
            mInfoView.showMarkerInfo(collection.get(0));
        }
    };


}
