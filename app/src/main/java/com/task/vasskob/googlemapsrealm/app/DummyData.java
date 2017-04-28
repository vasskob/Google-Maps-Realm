package com.task.vasskob.googlemapsrealm.app;

import android.app.Activity;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.presenter.MapsPresenterImpl;

import java.util.ArrayList;

public class DummyData {

    public static void setRealmDummyMarkers(Activity activity) {
        MapsPresenterImpl presenter=new MapsPresenterImpl();
        ArrayList<Marker> markers = new ArrayList<>();

        Marker marker = new Marker();
        marker.setId(1);
        marker.setTitle("Barcelona");
        marker.setMarkerIcon(new MarkerIcon(1, R.drawable.ic_icon1));
        marker.setLatitude(41.23d);
        marker.setLongitude(2.09d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(2);
        marker.setTitle("Paris");
        marker.setMarkerIcon(new MarkerIcon(2, R.drawable.ic_icon2));
        marker.setLatitude(48.48d);
        marker.setLongitude(2.20d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(3);
        marker.setTitle("Rome");
        marker.setMarkerIcon(new MarkerIcon(3, R.drawable.ic_icon3));
        marker.setLatitude(41.54d);
        marker.setLongitude(12.27d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(4);
        marker.setTitle("Tokyo");
        marker.setMarkerIcon(new MarkerIcon(4, R.drawable.ic_icon4));
        marker.setLatitude(35.40d);
        marker.setLongitude(139.45d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(5);
        marker.setTitle("Sydney");
        marker.setMarkerIcon(new MarkerIcon(5, R.drawable.ic_default_marker));
        marker.setLatitude(-34d);
        marker.setLongitude(151d);
        markers.add(marker);

        for (Marker m : markers) {
            presenter.addMarkerToRealm(m);
        }
        Prefs.with(activity).setPreLoad(true);
    }

}
