package com.task.vasskob.googlemapsrealm;

import android.app.Activity;

import com.task.vasskob.googlemapsrealm.app.Prefs;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import java.util.ArrayList;

import static com.task.vasskob.googlemapsrealm.realm.DbOperations.writeMarkerToRealm;

public class DummyData {

    public static void setRealmDummyMarkers(Activity activity) {

        ArrayList<Marker> markers = new ArrayList<>();

        Marker marker = new Marker();
        marker.setId(1 + System.currentTimeMillis());
        marker.setLabel("Barcelona");
        marker.setMarkerIcon(new MarkerIcon(1, R.drawable.ic_icon1));
        marker.setLatitude(41.23d);
        marker.setLongitude(2.09d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(2 + System.currentTimeMillis());
        marker.setLabel("Paris");
        marker.setMarkerIcon(new MarkerIcon(2, R.drawable.ic_icon2));
        marker.setLatitude(48.48d);
        marker.setLongitude(2.20d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(3 + System.currentTimeMillis());
        marker.setLabel("Rome");
        marker.setMarkerIcon(new MarkerIcon(3, R.drawable.ic_icon3));
        marker.setLatitude(41.54d);
        marker.setLongitude(12.27d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(4 + System.currentTimeMillis());
        marker.setLabel("Tokyo");
        marker.setMarkerIcon(new MarkerIcon(4, R.drawable.ic_icon4));
        marker.setLatitude(35.40d);
        marker.setLongitude(139.45d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(5 + System.currentTimeMillis());
        marker.setLabel("Sydney");
        marker.setMarkerIcon(new MarkerIcon(5, R.drawable.ic_default_marker));
        marker.setLatitude(-34d);
        marker.setLongitude(151d);
        markers.add(marker);

        for (Marker m : markers) {
            writeMarkerToRealm(m);
        }

        Prefs.with(activity).setPreLoad(true);
    }

}
