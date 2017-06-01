package com.task.vasskob.googlemapsrealm.screens.map.presenter;

import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerRealm;
import com.task.vasskob.googlemapsrealm.screens.common.model.mapper.MarkerRealmToMarkerMapper;
import com.task.vasskob.googlemapsrealm.screens.common.model.mapper.MarkerToMarkerRealmMaper;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.AllMarkersSpecification;
import com.task.vasskob.googlemapsrealm.screens.common.model.repository.MarkerRealmRepository;
import com.task.vasskob.googlemapsrealm.screens.common.presenter.BasePresenter;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.map.view.MapsView;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedCollectionChangeSet;
import io.realm.OrderedRealmCollectionChangeListener;
import io.realm.RealmResults;

public class MapsPresenterImpl extends BasePresenter implements MapsPresenter<MapsView> {


    private final MarkerRealmRepository mRealmRepository;
    private MapsView mMapsView;
    private List<MarkerRealm> markerRealmList;
    private List<Marker> markers;
    private OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>> callback = new OrderedRealmCollectionChangeListener<RealmResults<MarkerRealm>>() {
        @Override
        public void onChange(RealmResults<MarkerRealm> collection, OrderedCollectionChangeSet changeSet) {
            markers.clear();
            for (MarkerRealm markerRealm : collection) {
                markers.add(new MarkerRealmToMarkerMapper().map(markerRealm));
            }
            mMapsView.showMarkersOnMap(markers);
        }
    };

    public MapsPresenterImpl() {
        mRealmRepository = (MarkerRealmRepository) realmRepository;
        markerRealmList = new ArrayList<>();
        markers = new ArrayList<>();
    }

    @Override
    public void showMarkersOnMap() {
        mRealmRepository.query(new AllMarkersSpecification(callback));
    }

    @Override
    public void addMarkerToRealm(Marker marker) {
        mRealmRepository.add(new MarkerToMarkerRealmMaper().map(marker));
    }

    public void addMarkerListToRealm(List<Marker> markers) {
        markerRealmList.clear();
        for (Marker marker : markers) {
            markerRealmList.add(new MarkerToMarkerRealmMaper().map(marker));
        }
        mRealmRepository.add(markerRealmList);
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
