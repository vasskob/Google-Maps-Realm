package com.task.vasskob.googlemapsrealm.app;

import android.app.Activity;

import com.google.android.gms.maps.model.LatLng;
import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.presenter.MapsPresenterImpl;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.task.vasskob.googlemapsrealm.app.MyApplication.getMarkerIconsList;

public class DummyData {

    private static final int AMOUNT_OF_MARKERS = 10000;
    private static final double MIN_LAT_LNG = -180d;
    private static final double MAX_LAT_LNG = 180d;
    private static final String DEFAULT_MARKER_TITLE = "Marker № ";

    public static void setRealmDummyMarkers(Activity activity) {
        MapsPresenterImpl presenter = new MapsPresenterImpl();
        ArrayList<Marker> markers = new ArrayList<>();

        Marker marker = new Marker();
        marker.setId("1");
        marker.setTitle("Barcelona");
        marker.setMarkerIcon(new MarkerIcon(1, R.drawable.ic_icon1));
        marker.setLatitude(41.23d);
        marker.setLongitude(2.09d);
        markers.add(marker);

        marker = new Marker();
        marker.setId("2");
        marker.setTitle("Paris");
        marker.setMarkerIcon(new MarkerIcon(2, R.drawable.ic_icon2));
        marker.setLatitude(48.48d);
        marker.setLongitude(2.20d);
        markers.add(marker);

        marker = new Marker();
        marker.setId("3");
        marker.setTitle("Rome");
        marker.setMarkerIcon(new MarkerIcon(3, R.drawable.ic_icon3));
        marker.setLatitude(41.54d);
        marker.setLongitude(12.27d);
        markers.add(marker);

        marker = new Marker();
        marker.setId("4");
        marker.setTitle("Tokyo");
        marker.setMarkerIcon(new MarkerIcon(4, R.drawable.ic_icon4));
        marker.setLatitude(35.40d);
        marker.setLongitude(139.45d);
        markers.add(marker);

        marker = new Marker();
        marker.setId("5");
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

    public static void setRandomMarkersToRealm() {
        MapsPresenterImpl presenter = new MapsPresenterImpl();
        ArrayList<Marker> markers = new ArrayList<>();
        Marker marker;
        for (int i = 0; i < AMOUNT_OF_MARKERS; i++) {

            marker = new Marker();
            marker.setId(UUID.randomUUID().toString());
            marker.setTitle(DEFAULT_MARKER_TITLE + i);
            LatLng latLng = getRandomLatLng();
            marker.setLatitude(latLng.latitude);
            marker.setLongitude(latLng.longitude);
            marker.setMarkerIcon(getRandomMarkerIcon());
            markers.add(marker);
        }
        presenter.addMarkerListToRealm(markers);
    }

    private static LatLng getRandomLatLng() {
        Random r = new Random();
        double randomLat = MIN_LAT_LNG + (MAX_LAT_LNG - MIN_LAT_LNG) * r.nextDouble();
        double randomLng = MIN_LAT_LNG + (MAX_LAT_LNG - MIN_LAT_LNG) * r.nextDouble();
        return new LatLng(randomLat, randomLng);
    }

    private static MarkerIcon getRandomMarkerIcon() {
        Random r = new Random();
        List<MarkerIcon> icons = getMarkerIconsList();
        int index = r.nextInt(icons.size());
        return icons.get(index);
    }
}
