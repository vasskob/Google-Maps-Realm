package com.task.vasskob.googlemapsrealm.screens.marker_details.presenter;

import com.task.vasskob.googlemapsrealm.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerByIdSpecification;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemapsrealm.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.marker_details.view.MarkerInfoView;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView>, OnMarkerChangeClickListener {

    private final MarkerRealmRepository mRealmRepository;
    private MarkerInfoView mInfoView;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            if (mInfoView != null && !collection.isEmpty()) {
                mInfoView.showMarkerInfo(collection.get(0));
            }
        }
    };

    public MarkerInfoPresenterImpl() {
        mRealmRepository = (MarkerRealmRepository) realmRepository;
        mRealmRepository.setListener(this);
    }

    @Override
    public void showMarkerInfoById(String id) {
        mRealmRepository.query(new MarkerByIdSpecification(id, callback));
    }

    @Override
    public void updateMarkerInDb(Marker marker, String title, MarkerIcon markerIcon) {
        mRealmRepository.update(marker, title, markerIcon);
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        mRealmRepository.delete(marker);
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
