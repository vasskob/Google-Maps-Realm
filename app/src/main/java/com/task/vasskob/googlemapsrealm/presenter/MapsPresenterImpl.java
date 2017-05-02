package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.view.MapsView;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private MapsView mMapsView;

    @Override
    public void showMarkersOnMap() {
        //RealmResults<Marker> markers =
        //    realmController.setOnChangeListener(this);
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
        //  realmController.setOnChangeListener(callback);
        realmController.getAllMarkers();
//        results.load();
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


//    @Override
//    public void onChangeM(RealmResults<Marker> results) {
//        mMapsView.showMarkers(results);
//        //mMapsView.
//    }


}
