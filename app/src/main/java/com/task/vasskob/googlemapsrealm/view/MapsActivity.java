package com.task.vasskob.googlemapsrealm.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.app.Prefs;
import com.task.vasskob.googlemapsrealm.listener.ErrorListener;
import com.task.vasskob.googlemapsrealm.listener.MultiplePermissionListener;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerToMarkerOptionsMapper;
import com.task.vasskob.googlemapsrealm.realm.DbOperations;
import com.task.vasskob.googlemapsrealm.realm.RealmController;
import com.task.vasskob.googlemapsrealm.view.dialog.MarkerDialogFragment;

import butterknife.Bind;
import io.realm.RealmResults;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.task.vasskob.googlemapsrealm.app.DummyData.setRealmDummyMarkers;
import static com.task.vasskob.googlemapsrealm.R.id.map;
import static com.task.vasskob.googlemapsrealm.realm.DbOperations.writeMarkerToRealm;

public class MapsActivity extends AppCompatActivity implements MapView,OnMapReadyCallback, MarkerDialogFragment.OnDialogFragmentClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MARKER_ID = "id";
    public static final String DIALOG_FRAGMENT_TAG = "dialog";
    private GoogleMap mMap;


    @Bind(R.id.et_marker_title)
    EditText edt;

    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    private LatLng mLatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);

        new DbOperations(getApplication());

        if (!checkPermissions()) {
            createPermissionListeners();
        }

        if (!Prefs.with(this).getPreLoad()) {
            setRealmDummyMarkers(this);  // Add dummy markers to db if app run for the first time
            Log.d("Nadsf", "prefs is false");
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_HYBRID);

        setCurrentLocation();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 200, 0, 100);



        RealmController.with(this).refresh();
        showMarkersOnMap();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showAddMarkerDialog();
                mLatLng = latLng;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
                Intent intent = new Intent(MapsActivity.this, MarkerInfoActivity.class);
                intent.putExtra(MARKER_ID, String.valueOf(marker.getTag()));

                Log.d(TAG, "onInfoWindowClick " + marker.getTag());
                startActivity(intent);
            }
        });
    }

    public void setCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Log.d(TAG, " setCurrentLocation Permission deny!");
        }
    }

    private void showMarkersOnMap() {
        //   TODO: 25/04/17 why on ui thread? https://realm.io/docs/java/latest/#asynchronous-queries
        // https://github.com/DragonJik/BankSecurityCard
        // https://realm.io/docs/java/latest/#asynchronous-queries
        RealmResults<Marker> markers = RealmController.with(this).getMarkers();
        markers.load();
        for (Marker marker : markers) {
            mMap.addMarker(new MarkerToMarkerOptionsMapper().map(marker)).setTag(marker.getId());
        }


    }
//
//    private RealmMarkerChangeListener callback = new RealmMarkerChangeListener() {
//        @Override
//        public void onChange() {
//
//        }
//
//        @Override
//        public void OnChange(RealmResults<Marker> markers) {
//            for (Marker marker : markers) {
//                mMap.addMarker(new MarkerToMarkerOptionsMapper().map(marker)).setTag(marker.getId());
//            }
//        }
//    };
//
//    @Override
//    public void onStart() {
//        super.onStart();
//        markers.addChangeListener(callback);
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        markers.removeChangeListener(callback);
//    }

//    private interface RealmMarkerChangeListener extends RealmChangeListener {
//        void OnChange(RealmResults<Marker> results);
//    }


    private void showAddMarkerDialog() {
        MarkerDialogFragment mdf =
                MarkerDialogFragment.newInstance(R.string.new_marker_dialog_title);
        mdf.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }


//////////////////  Real Time Permission Section ///////////////////////////////

    private void createPermissionListeners() {

        ErrorListener errorListener = new ErrorListener();
        MultiplePermissionsListener mpl = new MultiplePermissionListener(this);

        MultiplePermissionsListener cmpl = new CompositeMultiplePermissionsListener(mpl,
                DialogOnAnyDeniedMultiplePermissionsListener.Builder.withContext(MapsActivity.this)
                        .withMessage(R.string.permission_message)
                        .withButtonText(android.R.string.ok)
                        .withTitle(R.string.permission_title)
                        .build());

        Dexter.withActivity(this)
                .withPermissions(
                        ACCESS_COARSE_LOCATION,
                        ACCESS_FINE_LOCATION)
                .withListener(cmpl)
                .withErrorListener(errorListener)
                .check();
    }

    private boolean checkPermissions() {
        int resultCoarse = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_COARSE_LOCATION);
        int resultFine = ContextCompat.checkSelfPermission(getApplicationContext(), ACCESS_FINE_LOCATION);
        return resultFine == PackageManager.PERMISSION_GRANTED && resultCoarse == PackageManager.PERMISSION_GRANTED;
    }

    /////////////////////// Menu Options Section ////////////////////////
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int itemId = item.getItemId();
        switch (itemId) {
            case R.id.normal_type:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.normal_style);
                break;
            case R.id.night_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.night_style);
                break;
            case R.id.retro_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.retro_style);
                break;
            case R.id.silver_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.silver_style);
                break;
            case R.id.dark_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.dark_style);
                break;
            case R.id.aubergine_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setMapStyle(R.raw.aubergine_style);
                break;
            case R.id.satellite_type:
                mMap.setMapType(MAP_TYPE_SATELLITE);
                break;
            case R.id.hybrid_type:
                mMap.setMapType(MAP_TYPE_HYBRID);
                break;
            case R.id.terrain_type:
                mMap.setMapType(MAP_TYPE_TERRAIN);
                break;
            default:
                return false;
        }
        return true;
    }

    private void setMapStyle(int style) {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, style));
            if (!success) {
                Log.e(TAG, "Style parsing failed !");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        RealmController.with(this).refresh();
        mMap.clear();
        showMarkersOnMap();
    }

    @Override
    public void onDoneClicked(MarkerDialogFragment dialog) {

        Marker marker = new Marker();
        marker.setId(RealmController.getInstance().getMarkers().size() + System.currentTimeMillis());
        marker.setLatitude(mLatLng.latitude);
        marker.setLongitude(mLatLng.longitude);
        marker.setTitle(dialog.getMarkerTitle());
        marker.setMarkerIcon(dialog.getMarkerIcon());

        addMarkerOnMap(marker);
        writeMarkerToRealm(marker);

    }

    private void addMarkerOnMap(Marker marker) {
        String mTitle = marker.getTitle();
        int mIconResId = marker.getMarkerIcon().getResId();
        BitmapDescriptor markerIcon = BitmapDescriptorFactory.fromResource(mIconResId);
        mMap.addMarker(new MarkerOptions().position(mLatLng).title(mTitle).icon(markerIcon)).setTag(marker.getId());
    }

    @Override
    public void showMarkerOnMap(Marker marker) {

    }

    @Override
    public void showToast(String msg) {

    }
}
