package com.task.vasskob.googlemaps.screens.map.presenter;

import android.util.Log;

import com.task.vasskob.googlemaps.app.MyApplication;
import com.task.vasskob.googlemaps.listeners.db.OnDataLoadedListener;
import com.task.vasskob.googlemaps.screens.common.model.repository.AllMarkersSpecification;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;
import com.task.vasskob.googlemaps.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.screens.map.view.MapsView;
import com.task.vasskob.googlemaps.utils.Prefs;

import java.util.List;

import javax.inject.Inject;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView>, OnDataLoadedListener {

    private static final String TAG = MapsPresenterImpl.class.getSimpleName();
    private MapsView mMapsView;
    @Inject
    public Prefs prefs;

    public MapsPresenterImpl(MarkerRepository repository) {
        super(repository);
        MyApplication.getInstance().getMyAppComponent().inject(this);
    }

    @Override
    public void showMarkersOnMap() {
        repository.query(new AllMarkersSpecification(this));
        Log.d(TAG, "showMarkersOnMap: sharedPrefs = " + prefs.getPreLoad());
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
    public void onDataReady(List<Marker> markerList) {
        mMapsView.showMarkersOnMap(markerList);
    }
}
