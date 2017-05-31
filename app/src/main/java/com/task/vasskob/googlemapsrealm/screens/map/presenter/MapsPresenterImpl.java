package com.task.vasskob.googlemapsrealm.screens.map.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.model.repository.AllMarkersSpecification;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemapsrealm.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.map.view.MapsView;

import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private final MarkerRealmRepository mRealmRepository;
    private MapsView mMapsView;
    private OrderedRealmCollectionChangeListener<RealmResults<Marker>> callback = new OrderedRealmCollectionChangeListener<RealmResults<Marker>>() {
        @Override
        public void onChange(RealmResults<Marker> collection, OrderedCollectionChangeSet changeSet) {
            mMapsView.showMarkersOnMap(collection);
        }
    };

    public MapsPresenterImpl() {
        mRealmRepository = (MarkerRealmRepository) realmRepository;
    }

    @Override
    public void showMarkersOnMap() {
        mRealmRepository.query(new AllMarkersSpecification(callback));
    }

    @Override
    public void addMarkerToRealm(Marker marker) {
        mRealmRepository.add(marker);
    }


    public void addMarkerListToRealm(List<Marker> markers) {
        mRealmRepository.add(markers);
    }

    @Override
    public void removeListener() {
        mRealmRepository.removeListener();
    }

    @Override
    public void setView(MapsView view) {
        mMapsView = view;
    }

    @Override
    public void clearView() {
        mMapsView = null;
    }
}
