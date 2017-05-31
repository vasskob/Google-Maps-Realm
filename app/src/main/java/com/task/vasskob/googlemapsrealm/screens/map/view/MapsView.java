package com.task.vasskob.googlemapsrealm.screens.map.view;

import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerRealm;

import io.realm.RealmResults;

public interface MapsView {

  void showMarkersOnMap(final RealmResults<MarkerRealm> markers);
}
