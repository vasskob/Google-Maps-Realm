package com.task.vasskob.googlemapsrealm.screens.map.view;

import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;

import java.util.List;

public interface MapsView {

  void showMarkersOnMap(final List<Marker> markers);
}
