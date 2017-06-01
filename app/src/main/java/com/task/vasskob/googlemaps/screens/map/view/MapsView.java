package com.task.vasskob.googlemaps.screens.map.view;

import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.List;

public interface MapsView {

  void showMarkersOnMap(final List<Marker> markers);
}
