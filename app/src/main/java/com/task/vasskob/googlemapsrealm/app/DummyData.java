package com.task.vasskob.googlemapsrealm.app;

import android.app.Activity;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;
import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.map.presenter.MapsPresenterImpl;
import com.task.vasskob.googlemapsrealm.utils.Prefs;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.UUID;

import static com.task.vasskob.googlemapsrealm.app.MyApplication.getMarkerIconsList;

public class DummyData {

    private static final int AMOUNT_OF_MARKERS = 100;
    private static final double MIN_LAT_LNG = -180d;
    private static final double MAX_LAT_LNG = 180d;
    private static String randomMarkerTitle;
    private static final int RADIUS = 1000000;
    public final static LatLng CENTER = new LatLng(49.0139d, 31.2858d);

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
        randomMarkerTitle = activity.getResources().getString(R.string.random_marker_label);
    }

    public static void setRandomMarkersToRealm() {
        MapsPresenterImpl presenter = new MapsPresenterImpl();
        ArrayList<Marker> markers = new ArrayList<>();
        Marker marker;
        List<MarkerIcon> icons = getMarkerIconsList();
        for (int i = 0; i < AMOUNT_OF_MARKERS; i++) {

            marker = new Marker();
            marker.setId(UUID.randomUUID().toString());
            marker.setTitle(randomMarkerTitle + i);
            //   LatLng latLng = getRandomLatLng();
            LatLng latLng = getRandomLatLng(CENTER, RADIUS);
            marker.setLatitude(latLng.latitude);
            marker.setLongitude(latLng.longitude);
            marker.setMarkerIcon(getRandomMarkerIconFromList(icons));
            markers.add(marker);
        }
        presenter.addMarkerListToRealm(markers);
    }

    private static MarkerIcon getRandomMarkerIconFromList(List<MarkerIcon> icons) {
        Random r = new Random();
        int index = r.nextInt(icons.size());
        return icons.get(index);
    }

    private static LatLng getRandomLatLng(LatLng point, int radius) {
        List<LatLng> randomPoints = new ArrayList<>();
        List<Float> randomDistances = new ArrayList<>();
        Location myLocation = new Location("");
        myLocation.setLatitude(point.latitude);
        myLocation.setLongitude(point.longitude);

        //This is to generate 10 random points
        for (int i = 0; i < 10; i++) {
            double x0 = point.latitude;
            double y0 = point.longitude;

            Random random = new Random();

            // Convert radius from meters to degrees
            double radiusInDegrees = radius / 111000f;

            double u = random.nextDouble();
            double v = random.nextDouble();
            double w = radiusInDegrees * Math.sqrt(u);
            double t = 2 * Math.PI * v;
            double x = w * Math.cos(t);
            double y = w * Math.sin(t);

            // Adjust the x-coordinate for the shrinking of the east-west distances
            double new_x = x / Math.cos(y0);

            double foundLatitude = new_x + x0;
            double foundLongitude = y + y0;
            LatLng randomLatLng = new LatLng(foundLatitude, foundLongitude);
            randomPoints.add(randomLatLng);
            Location l1 = new Location("");
            l1.setLatitude(randomLatLng.latitude);
            l1.setLongitude(randomLatLng.longitude);
            randomDistances.add(l1.distanceTo(myLocation));
        }
        //Get nearest point to the centre
        int indexOfNearestPointToCentre = randomDistances.indexOf(Collections.min(randomDistances));
        return randomPoints.get(indexOfNearestPointToCentre);
    }

    @SuppressWarnings("unused")
    private static LatLng getRandomLatLng() {
        Random r = new Random();
        double randomLat = MIN_LAT_LNG + (MAX_LAT_LNG - MIN_LAT_LNG) * r.nextDouble();
        double randomLng = MIN_LAT_LNG + (MAX_LAT_LNG - MIN_LAT_LNG) * r.nextDouble();
        return new LatLng(randomLat, randomLng);
    }
}
