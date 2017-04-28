package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.realm.RealmController;
import com.task.vasskob.googlemapsrealm.view.MapsView;

import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView>, RealmController.OnTransactionCallback {


    private MapsView mMapsView;

    @Override
    public void showMarkersOnMap() {
        RealmResults<Marker> markers = realmController.getAllMarkers();
        mMapsView.showMarkers(markers);
    }

    @Override
    public void updateRealm() {
        realmController.refresh();
    }

    @Override
    public void addMarkerToRealm(Marker marker) {
      realmController.addMarkerToRealmAsync(marker, this);
    }

    @Override
    public void closeRealm() {
        realmController.closeRealm();
    }

    @Override
    public int getMarkersAmount() {
        return realmController.getAllMarkers().size();
    }

    @Override
    public void setView(MapsView view) {
        mMapsView = view;
    }

    @Override
    public void clearView() {
        mMapsView = null;
    }

    @Override
    public void onRealmSuccess() {
        mMapsView.showToastSuccess();
    }

    @Override
    public void onRealmError(Exception e) {
        mMapsView.showToastError();
    }
}
