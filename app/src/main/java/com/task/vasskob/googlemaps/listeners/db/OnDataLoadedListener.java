package com.task.vasskob.googlemaps.listeners.db;

import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.List;

public interface OnDataLoadedListener {
    void onDateReady(List<Marker> markerList);
}
