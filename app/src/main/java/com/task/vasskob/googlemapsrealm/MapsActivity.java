package com.task.vasskob.googlemapsrealm;

import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

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
import com.task.vasskob.googlemapsrealm.app.Prefs;
import com.task.vasskob.googlemapsrealm.listener.ErrorListener;
import com.task.vasskob.googlemapsrealm.listener.MultiplePermissionListener;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.realm.RealmController;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.task.vasskob.googlemapsrealm.R.id.map;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MISSING_TITLE_WARN = "Entry not saved, missing title";
    private Realm realm;
    private String mTitle;
    private GoogleMap mMap;


    int ICON_1 = 1;
    int ICON_2 = 2;
    int ICON_3 = 3;
    int ICON_4 = 4;
    int DEFAULT_ICON = 5;

    @Bind({R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4})
    List<ImageButton> mIcons;
    static final ButterKnife.Setter<View, Boolean> SELECT = new ButterKnife.Setter<View, Boolean>() {
        @Override
        public void set(View view, Boolean value, int index) {
            view.setSelected(value);
        }
    };
    private int selectedImageBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        ActionBar actionBar = getSupportActionBar();
        assert actionBar != null;
        actionBar.setDisplayShowTitleEnabled(false);

        if (!checkPermissions()) {
            createPermissionListeners();
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);

        realm = RealmController.with(this).getRealm();

    }

    @OnClick({R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4})
    public void onIconClick(View view) {
        Log.d(TAG, "onIconClick: id= " + view.getId());
        selectIcon(getTabPosition(view.getId()));
        selectedImageBtn = view.getId();
    }

    private void selectIcon(int position) {
        ButterKnife.apply(mIcons, SELECT, false);
        mIcons.get(position - 1).setSelected(true);

        //Log.d(TAG, "selectIcon: Tab selected  =  " + position);
    }

    private int getTabPosition(int tabID) {
        switch (tabID) {
            case R.id.icon1:
                return ICON_1;
            case R.id.icon2:
                return ICON_2;
            case R.id.icon3:
                return ICON_3;
            case R.id.icon4:
                return ICON_4;
            default:
                return DEFAULT_ICON;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_HYBRID);

        setCurrentLocation();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 200, 0, 100);

        if (!Prefs.with(this).getPreLoad()) {
            // Add dummy markers to db
            setRealmData();
        }

        RealmController.with(this).refresh();
        showMarkersOnMap();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showAddMarkerDialog(latLng);
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
        LatLng markerLatLng;
        String markerTitle;
        BitmapDescriptor markerIcon;
        RealmResults<Marker> markers = RealmController.with(this).getMarkers();

        for (Marker marker : markers) {
            markerLatLng = new LatLng(marker.getLatitude(), marker.getLongitude());
            markerTitle = marker.getLabel();
            markerIcon = BitmapDescriptorFactory.fromResource(manageMarkerIcon(marker.getIcon()));
            mMap.addMarker(new MarkerOptions().position(markerLatLng).title(markerTitle).icon(markerIcon));

        }

        //googleMap.moveCamera(CameraUpdateFactory.newLatLng(mylatLng));
    }

    private void showAddMarkerDialog(final LatLng latLng) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.custom_dialog, null);
        ButterKnife.bind(this, dialogView);
        dialogBuilder.setView(dialogView);

        final EditText edt = (EditText) dialogView.findViewById(R.id.edit1);

        dialogBuilder.setTitle(getResources().getString(R.string.add_marker));
        dialogBuilder.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mTitle = edt.getText().toString();
                Marker marker = new Marker();
                marker.setId(RealmController.getInstance().getMarkers().size() + System.currentTimeMillis());
                marker.setLabel(mTitle);
                marker.setLatitude(latLng.latitude);
                marker.setLongitude(latLng.longitude);
                marker.setIcon(manageReverseMarkerIcon(selectedImageBtn));

                if (edt.getText().toString().equals("")) {
                    Toast.makeText(MapsActivity.this, MISSING_TITLE_WARN, Toast.LENGTH_LONG).show();
                } else {
                    BitmapDescriptor markerIcon1 = BitmapDescriptorFactory.fromResource(manageMarkerIcon(manageReverseMarkerIcon(selectedImageBtn)));
                    mMap.addMarker(new MarkerOptions().position(latLng).title(mTitle).icon(markerIcon1));
                    writeToRealm(marker);
                }
            }
        });
        dialogBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
            }
        });
        AlertDialog b = dialogBuilder.create();
        b.show();
    }

    private void writeToRealm(Marker marker) {
        realm.beginTransaction();
        realm.copyToRealm(marker);
        realm.commitTransaction();
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
            writeToRealm(m);
        }

        Prefs.with(this).setPreLoad(true);

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
                return R.drawable.icon5;
        }
    }

    private String manageReverseMarkerIcon(int id) {
        switch (id) {
            case R.id.icon1:
                return "icon1";
            case R.id.icon2:
                return "icon2";
            case R.id.icon3:
                return "icon3";
            case R.id.icon4:
                return "icon4";
            default:
                return "icon5";
        }
    }

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
                setStyle(R.raw.normal_style);
                break;
            case R.id.night_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setStyle(R.raw.night_style);
                break;
            case R.id.retro_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setStyle(R.raw.retro_style);
                break;
            case R.id.silver_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setStyle(R.raw.silver_style);
                break;
            case R.id.dark_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setStyle(R.raw.dark_style);
                break;
            case R.id.aubergine_style:
                mMap.setMapType(MAP_TYPE_NORMAL);
                setStyle(R.raw.aubergine_style);
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

    private void setStyle(int style) {
        try {
            boolean success = mMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            this, style));

            if (!success) {
                Log.e(TAG, "Style parsing failed.");
            }
        } catch (Resources.NotFoundException e) {
            Log.e(TAG, "Can't find style. Error: ", e);
        }
    }
}
