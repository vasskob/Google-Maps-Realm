package com.task.vasskob.googlemaps.screens.map.view;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.maps.android.clustering.ClusterManager;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.CompositeMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.app.DummyData;
import com.task.vasskob.googlemaps.app.MyApplication;
import com.task.vasskob.googlemaps.listeners.permission.ErrorListener;
import com.task.vasskob.googlemaps.listeners.permission.MultiplePermissionListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerClusterItem;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerToMarkerOptionsMapper;
import com.task.vasskob.googlemaps.screens.common.model.repository.MarkerRepository;
import com.task.vasskob.googlemaps.screens.detail.view.MarkerInfoActivity;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.screens.map.presenter.MapsPresenterImpl;
import com.task.vasskob.googlemaps.screens.map.view.dialog.AddMarkerDialogFragment;
import com.task.vasskob.googlemaps.utils.Prefs;

import java.util.List;
import java.util.UUID;

import javax.inject.Inject;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.task.vasskob.googlemaps.R.id.map;

public class MapsActivity extends AppCompatActivity implements MapsView, OnMapReadyCallback, AddMarkerDialogFragment.OnDialogClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MARKER_ID = "id";
    public static final String DIALOG_FRAGMENT_TAG = "dialog";
    public static final int MAP_PADDING_TOP = 200;
    public static final int MAP_PADDING_BOTTOM = 100;
    private GoogleMap mMap;
    private LatLng mLatLng;
    private MapsPresenterImpl presenter;

    @Inject
    public MarkerRepository markerRepository;

    @Inject
    public Prefs sharedPrefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        ((MyApplication) getApplication()).getMyAppComponent().inject(this);

        if (!checkPermissions()) {
            createPermissionListeners();
        }

        presenter = new MapsPresenterImpl(markerRepository);
        presenter.setView(this);

        if (!sharedPrefs.getPreLoad()) {
            DummyData dymmyData = new DummyData(presenter);
            dymmyData.setRealmDummyMarkers();  // Add dummy markers to db if app run for the first time
            String randomMarkerTitle = getString(R.string.random_marker_label);
            dymmyData.setRandomMarkersToDb(randomMarkerTitle);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        initSearch();
    }

    private void initSearch() {
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                LatLng searchedPlace = place.getLatLng();
                CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(searchedPlace, 12.0f);
                mMap.animateCamera(cameraUpdate);
                Log.i(TAG, "Place: " + place.getName());
            }

            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        configureMap();
        setCurrentLocation();

        presenter.showMarkersOnMap();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                Log.d(TAG, "onMapLongClick: ");
                showAddMarkerDialog();
                mLatLng = latLng;
            }
        });
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(com.google.android.gms.maps.model.Marker marker) {
                showMarkerInfoView(marker);
            }
        });
    }

    private void configureMap() {
        mMap.setMapType(MAP_TYPE_HYBRID);
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, MAP_PADDING_TOP, 0, MAP_PADDING_BOTTOM);
    }

    public void setCurrentLocation() {
        if (ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        } else {
            Log.d(TAG, " setCurrentLocation Permission deny!");
        }
    }

    private void showAddMarkerDialog() {
        AddMarkerDialogFragment mdf =
                AddMarkerDialogFragment.newInstance(R.string.new_marker_dialog_title);
        mdf.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    private void showMarkerInfoView(com.google.android.gms.maps.model.Marker marker) {
        Intent intent = new Intent(MapsActivity.this, MarkerInfoActivity.class);
        intent.putExtra(MARKER_ID, String.valueOf(marker.getTag()));
        Log.d(TAG, "onInfoWindowClick " + marker.getTag());
        startActivity(intent);
    }

    @SuppressWarnings("unused")
    private void setUpCluster() {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(DummyData.CENTER, 5));
        ClusterManager<MarkerClusterItem> mClusterManager = new ClusterManager<>(this, mMap);

        mMap.setOnCameraIdleListener(mClusterManager);
        mMap.setOnMarkerClickListener(mClusterManager);
    }

    @Override
    public void showMarkersOnMap(List<Marker> markers) {
        for (Marker marker : markers) {
            addMarkerOnMap(marker);
        }
    }

    private void addMarkerOnMap(Marker marker) {
        mMap.addMarker(new MarkerToMarkerOptionsMapper().map(marker)).setTag(marker.getId());
    }

    @Override
    public void onAddClicked(String title, MarkerIcon markerIcon) {
        Marker marker = new Marker();
        marker.setId(UUID.randomUUID().toString());
        marker.setLatitude(mLatLng.latitude);
        marker.setLongitude(mLatLng.longitude);
        marker.setTitle(title);
        marker.setMarkerIcon(markerIcon);

        addMarkerOnMap(marker);
        presenter.addMarkerToDb(marker);
    }

    @Override
    protected void onStop() {
        super.onStop();
        //  presenter.clearView();
        presenter.removeListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.clearView();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        //    presenter.setView(this);
        mMap.clear();
        presenter.showMarkersOnMap();
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
}
