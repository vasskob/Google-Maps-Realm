package com.task.vasskob.googlemapsrealm.view;

import com.task.vasskob.googlemapsrealm.model.Marker;

import io.realm.RealmResults;

public interface MapsView {

  void showMarkers(final RealmResults<Marker> markers);

  // TODO: 03/05/17 use meaningful names. Methods don't used, do you need them?
  void showToastSuccess();

  void showToastError();
}
