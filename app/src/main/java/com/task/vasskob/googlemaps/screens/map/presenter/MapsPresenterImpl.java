package com.task.vasskob.googlemaps.screens.map.presenter;

import com.task.vasskob.googlemaps.listeners.db.OnDataLoadedListener;
import com.task.vasskob.googlemaps.screens.common.model.repository.AllMarkersSpecification;
import com.task.vasskob.googlemaps.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.screens.map.view.MapsView;

import java.util.List;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView>, OnDataLoadedListener {

    private MapsView mMapsView;

    @Override
    public void showMarkersOnMap() {
        repository.query(new AllMarkersSpecification(this));
    }

    @Override
    public void addMarkerToDb(Marker marker) {
        repository.add(marker);
    }

    public void addMarkerListToDb(List<Marker> markers) {
        repository.add(markers);
    }

    @Override
    public void removeListener() {
        repository.removeListener();
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
    public void onDateReady(List<Marker> markerList) {
        mMapsView.showMarkersOnMap(markerList);
    }
}
