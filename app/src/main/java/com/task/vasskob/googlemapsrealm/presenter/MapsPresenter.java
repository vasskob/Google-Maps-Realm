package com.task.vasskob.googlemapsrealm.presenter;

import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

public interface MapsPresenter {
   void getMarkersFromDb();
   void onDialogIconChoose(MarkerIcon markerIcon);
}
