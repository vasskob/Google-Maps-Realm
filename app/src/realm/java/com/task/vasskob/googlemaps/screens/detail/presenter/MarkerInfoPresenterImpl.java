package com.task.vasskob.googlemaps.screens.detail.presenter;

import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerRealmToMarkerMapper;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerToMarkerRealmMaper;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerByIdSpecification;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemaps.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemaps.screens.detail.view.MarkerInfoView;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MarkerInfoPresenterImpl extends BasePresenter implements MarkerInfoPresenter<MarkerInfoView>, OnMarkerChangeClickListener {

    private final MarkerRealmRepository mRealmRepository;
    private MarkerInfoView mInfoView;
    private OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback = new OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>>() {
        @Override
        public void onChange(RealmResults<MarkerRealm> collection, OrderedCollectionChangeSet changeSet) {
            if (mInfoView != null && !collection.isEmpty()) {
                mInfoView.showMarkerInfo(new MarkerRealmToMarkerMapper().map(collection.get(0)));
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
    public void updateMarkerInDb(Marker marker) {
        mRealmRepository.update(new MarkerToMarkerRealmMaper().map(marker));
    }

    @Override
    public void deleteMarkerInDb(Marker marker) {
        mRealmRepository.delete(new MarkerToMarkerRealmMaper().map(marker));
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
