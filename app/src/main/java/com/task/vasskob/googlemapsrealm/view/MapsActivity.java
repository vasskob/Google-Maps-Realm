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
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
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
import com.task.vasskob.googlemapsrealm.presenter.MapsPresenterImpl;
import com.task.vasskob.googlemapsrealm.view.dialog.MarkerDialogFragment;

import java.util.UUID;

import butterknife.Bind;
import io.realm.RealmResults;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.task.vasskob.googlemapsrealm.R.id.map;
import static com.task.vasskob.googlemapsrealm.app.DummyData.setRealmDummyMarkers;

// TODO: 03/05/17 gpoup by packages
public class MapsActivity extends AppCompatActivity implements MapsView, OnMapReadyCallback, MarkerDialogFragment.OnDialogFragmentClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MARKER_ID = "id";
    public static final String DIALOG_FRAGMENT_TAG = "dialog";
    private GoogleMap mMap;


    @Bind(R.id.et_marker_title)
    EditText edt;

    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    private LatLng mLatLng;
    private MapsPresenterImpl presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        initActionBar();

        // TODO: 03/05/17 no need to check permissions, just call dexter
        if (!checkPermissions()) {
            createPermissionListeners();
        }

        if (!Prefs.with(this).getPreLoad()) {
            setRealmDummyMarkers(this);  // Add dummy markers to db if app run for the first time
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        presenter = new MapsPresenterImpl();
        presenter.setView(this);
    }

    private void initActionBar() {
        // TODO: 03/05/17 customize styles to remove actionar
        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);
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
        mMap.setPadding(0, 200, 0, 100);
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
        MarkerDialogFragment mdf =
                MarkerDialogFragment.newInstance(R.string.new_marker_dialog_title);
        mdf.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
    }

    private void showMarkerInfoView(com.google.android.gms.maps.model.Marker marker) {
        Intent intent = new Intent(MapsActivity.this, MarkerInfoActivity.class);
        intent.putExtra(MARKER_ID, String.valueOf(marker.getTag()));
        Log.d(TAG, "onInfoWindowClick " + marker.getTag());
        startActivity(intent);
    }

    @Override
    public void showMarkers(RealmResults<Marker> markers) {
        for (Marker marker : markers) {
            addMarkerOnMap(marker);
        }
    }

    @Override
    public void showToastSuccess() {
        Toast.makeText(this, R.string.marker_add_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showToastError() {
        Toast.makeText(this, R.string.marker_add_error, Toast.LENGTH_SHORT).show();
    }

    private void addMarkerOnMap(Marker marker) {
        mMap.addMarker(new MarkerToMarkerOptionsMapper().map(marker)).setTag(marker.getId());
    }

    @Override
    public void onAddClicked(MarkerDialogFragment dialog) {

        Marker marker = new Marker();
        marker.setId(UUID.randomUUID().toString());
        marker.setLatitude(mLatLng.latitude);
        marker.setLongitude(mLatLng.longitude);
        marker.setTitle(dialog.getMarkerTitle());
        marker.setMarkerIcon(dialog.getMarkerIcon());

        addMarkerOnMap(marker);
        presenter.addMarkerToRealm(marker);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.clearView();
        presenter.removeListener();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        presenter.setView(this);
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
