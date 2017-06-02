package com.task.vasskob.googlemaps.screens.detail.presenter;

import com.task.vasskob.googlemaps.listeners.db.OnDataLoadedListener;
import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerByIdSpecification;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;
import com.task.vasskob.googlemaps.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemaps.screens.detail.view.MarkerInfoView;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.List;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView>, OnMarkerChangeClickListener {

    private MarkerInfoView mInfoView;

    public MarkerInfoPresenterImpl(MarkerRepository repository) {
        super(repository);
        repository.setResultListener(this);
    }

    @Override
    public void showMarkerInfoById(String id) {
        repository.query(new MarkerByIdSpecification(id, new OnDataLoadedListener() {
            @Override
            public void onDateReady(List<Marker> markerList) {
                if (mInfoView != null && !markerList.isEmpty())
                    mInfoView.showMarkerInfo(markerList.get(0));
            }
        }));
    }

    @Override
    public void updateMarkerInDb(Marker marker) {
        repository.update(marker);
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        repository.delete(marker);
    }

    @Override
    public void setView(MarkerInfoView view) {
        mInfoView = view;
    }

    @Override
    public void clearView() {
        mInfoView = null;
    }

    @Override
    public void onSuccess() {
        mInfoView.closeActivity();
    }

    @Override
    public void onError() {
        mInfoView.showError();
    }
}
