package com.task.vasskob.googlemapsrealm;

import android.Manifest;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.realm.RealmController;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmResults;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private Realm realm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        realm = RealmController.with(this).getRealm();
    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
        googleMap.getUiSettings().setMyLocationButtonEnabled(true);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            googleMap.setMyLocationEnabled(true);
        } else {
            Log.d("onMapReady","Permission deny");
            // Show rationale and request permission.
        }

        // Add dummy markers to db
        setRealmData();
        RealmController.with(this).refresh();

        LatLng markerLatLng;
        String markerTitle;
        BitmapDescriptor markerIcon;
        RealmResults<Marker> markers = RealmController.with(this).getMarkers();

        for (Marker marker : markers) {
            markerLatLng = new LatLng(marker.getLatitude(), marker.getLongitude());
            markerTitle = marker.getLabel();
            markerIcon = BitmapDescriptorFactory.fromResource(manageMarkerIcon(marker.getIcon()));
            googleMap.addMarker(new MarkerOptions().position(markerLatLng).title(markerTitle).icon(markerIcon));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(markerLatLng));
        }


        // Add a marker in Sydney and move the camera


//        LatLng sydney = new LatLng(-34, 151);
//        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
//

        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private void setRealmData() {

        ArrayList<Marker> markers = new ArrayList<>();

        Marker marker = new Marker();
        marker.setId(1 + System.currentTimeMillis());
        marker.setLabel("Barcelona");
        marker.setIcon("icon1");
        marker.setLatitude(41.23d);
        marker.setLongitude(2.09d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(2 + System.currentTimeMillis());
        marker.setLabel("Paris");
        marker.setIcon("icon2");
        marker.setLatitude(48.48d);
        marker.setLongitude(2.20d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(3 + System.currentTimeMillis());
        marker.setLabel("Rome");
        marker.setIcon("icon3");
        marker.setLatitude(41.54d);
        marker.setLongitude(12.27d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(4 + System.currentTimeMillis());
        marker.setLabel("Tokyo");
        marker.setIcon("icon4");
        marker.setLatitude(35.40d);
        marker.setLongitude(139.45d);
        markers.add(marker);

        marker = new Marker();
        marker.setId(5 + System.currentTimeMillis());
        marker.setLabel("Sydney");
        marker.setIcon("icon1");
        marker.setLatitude(-34d);
        marker.setLongitude(151d);
        markers.add(marker);

        for (Marker m : markers) {
            // Persist your data easily
            realm.beginTransaction();
            realm.copyToRealm(m);
            realm.commitTransaction();
        }

        //    Prefs.with(this).setPreLoad(true);

    }

    private int manageMarkerIcon(String markerIcon) {
        switch (markerIcon) {
            case "icon1":
                return R.drawable.icon1;
            case "icon2":
                return R.drawable.icon2;
            case "icon3":
                return R.drawable.icon3;
            case "icon4":
                return R.drawable.icon4;
            default:
                return R.drawable.icon_default;
        }
    }

}
