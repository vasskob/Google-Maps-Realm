package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.view.MarkerInfoView;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView> {

    private MarkerInfoView mInfoView;

    @Override
    public void showMarkerInfoById(int id) {
        Marker marker = realmController.getMarker(id);
        marker.load();
        mInfoView.showMarkerInfo(marker);
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

}
