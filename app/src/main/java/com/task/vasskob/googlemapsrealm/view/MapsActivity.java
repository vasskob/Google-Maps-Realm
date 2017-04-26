package com.task.vasskob.googlemapsrealm.view;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
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
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.realm.DbOperations;
import com.task.vasskob.googlemapsrealm.realm.RealmController;
import com.task.vasskob.googlemapsrealm.view.dialog.MarkerDialogFragment;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.Realm;
import io.realm.RealmResults;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.view.View.inflate;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_HYBRID;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_NORMAL;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_SATELLITE;
import static com.google.android.gms.maps.GoogleMap.MAP_TYPE_TERRAIN;
import static com.task.vasskob.googlemapsrealm.DummyData.setRealmDummyMarkers;
import static com.task.vasskob.googlemapsrealm.R.id.map;
import static com.task.vasskob.googlemapsrealm.realm.DbOperations.writeMarkerToRealm;
import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.getMarkerIcon;
import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.manageMarkerIcon;
import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.manageReverseMarkerIcon;

public class MapsActivity extends AppCompatActivity implements OnMapReadyCallback, MarkerDialogFragment.OnDialogFragmentClickListener {

    private static final String TAG = MapsActivity.class.getSimpleName();
    public static final String MISSING_TITLE_WARN = "Entry not saved, missing title";
    public static final String MARKER_ID = "id";
    private static final int COUNT_OF_COLUMN = 4;

    private String mTitle;
    private GoogleMap mMap;


    int ICON_1 = 1;
    int ICON_2 = 2;
    int ICON_3 = 3;
    int ICON_4 = 4;
    int DEFAULT_ICON = 5;

    @Bind(R.id.edit1)
    EditText edt;

    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    //    @Bind({R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4})
//    List<ImageButton> mIcons;
//    static final ButterKnife.Setter<View, Boolean> SELECT = new ButterKnife.Setter<View, Boolean>() {
//        @Override
//        public void set(View view, Boolean value, int index) {
//            view.setSelected(value);
//        }
//    };
    private int selectedImageBtn;
    private ArrayList<MarkerIcon> markerIcons;

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

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);
    }

//    @OnClick({R.id.icon1, R.id.icon2, R.id.icon3, R.id.icon4})
//    public void onIconClick(View view) {
//        Log.d(TAG, "onIconClick: id= " + view.getId());
//        selectIcon(getTabPosition(view.getId()));
//        selectedImageBtn = view.getId();
//
//    }

//    private void selectIcon(int position) {
//        ButterKnife.apply(mIcons, SELECT, false);
//        mIcons.get(position - 1).setSelected(true);
//    }

//    private int getTabPosition(int tabID) {
//        switch (tabID) {
//            case R.id.icon1:
//                return ICON_1;
//            case R.id.icon2:
//                return ICON_2;
//            case R.id.icon3:
//                return ICON_3;
//            case R.id.icon4:
//                return ICON_4;
//            default:
//                return DEFAULT_ICON;
//        }
//    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(MAP_TYPE_HYBRID);

        setCurrentLocation();
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setPadding(0, 200, 0, 100);

        if (!Prefs.with(this).getPreLoad()) {
            //      setMarkerIconData(this);
            setRealmDummyMarkers(this);  // Add dummy markers to db if app run for the first time
        }

        markerIcons = new ArrayList<>();
        markerIcons.add(new MarkerIcon(1, R.drawable.ic_icon1));
        markerIcons.add(new MarkerIcon(2, R.drawable.ic_icon2));
        markerIcons.add(new MarkerIcon(3, R.drawable.ic_icon3));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));

        //  markerIcons.add(new MarkerIcon(5, R.drawable.ic_default_marker));


        RealmController.with(this).refresh();
        showMarkersOnMap();

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                showAddMarkerDialog(latLng);
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
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(41.54d, 12.27d)//WTF hardcoded?
                , 8));

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
        //   TODO: 25/04/17 why on ui thread? https://realm.io/docs/java/latest/#asynchronous-queries
        // ttps://github.com/DragonJik/BankSecurityCard
        RealmResults<Marker> markers = RealmController.with(this).getMarkers();

        for (Marker marker : markers) {
            // TODO: 25/04/17 create mapper for Marker-MarkerOptions relation to convert data
            markerLatLng = new LatLng(marker.getLatitude(), marker.getLongitude());
            markerTitle = marker.getLabel();
            markerIcon = BitmapDescriptorFactory.fromResource((marker.getMarkerIcon().getResId()));
            mMap.addMarker(new MarkerOptions().position(markerLatLng).title(markerTitle).icon(markerIcon)).setTag(marker.getId());
        }

    }





    private void showAddMarkerDialog(final LatLng latLng) {
        // TODO: 25/04/17 create separated CreateMarkerDialog with custom listener and other stuff
        // http://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android

        MarkerDialogFragment generalDialogFragment =
                MarkerDialogFragment.newInstance("title");
        generalDialogFragment.show(getSupportFragmentManager(),"dialog");

//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
//        final View dialogView = inflate(this, R.layout.custom_dialog, null);
//        ButterKnife.bind(this, dialogView);
//
//        rvMarkerIcons.setHasFixedSize(true);
//        rvMarkerIcons.setLayoutManager(new GridLayoutManager(this, COUNT_OF_COLUMN));
//
//        MarkerAdapter adapter = new MarkerAdapter(markerIcons, this);
//        rvMarkerIcons.setAdapter(adapter);
//
//        selectedImageBtn = 0;
//        dialogBuilder.setView(dialogView);
//        dialogBuilder.setTitle(getResources().getString(R.string.add_marker));
//        dialogBuilder.setPositiveButton(R.string.done, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                mTitle = edt.getText().toString();
//                Marker marker = new Marker();
//                marker.setId(RealmController.getInstance().getMarkers().size() + System.currentTimeMillis());
//                marker.setLabel(mTitle);
//                marker.setLatitude(latLng.latitude);
//                marker.setLongitude(latLng.longitude);
////                marker.setIcon(manageReverseMarkerIcon(selectedImageBtn));
//                if (selectedImageBtn == 0) {
//                    marker.setMarkerIcon(new MarkerIcon(5, R.drawable.ic_default_marker));
//                } else {
//                    marker.setMarkerIcon(new MarkerIcon(getMarkerIcon(selectedImageBtn), getMarkerIcon(selectedImageBtn)));
//                }
//
//
//                if (edt.getText().toString().equals("")) {
//                    Toast.makeText(MapsActivity.this, MISSING_TITLE_WARN, Toast.LENGTH_LONG).show();
//                } else {
//                    BitmapDescriptor markerIcon1 = BitmapDescriptorFactory.fromResource(manageMarkerIcon(manageReverseMarkerIcon(selectedImageBtn)));
//                    mMap.addMarker(new MarkerOptions().position(latLng).title(mTitle).icon(markerIcon1)).setTag(marker.getId());
//                    writeMarkerToRealm(marker);
//                }
//            }
//        });
//        dialogBuilder.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                dialog.dismiss();
//            }
//        });
//        AlertDialog b = dialogBuilder.create();
//        b.show();
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
                Log.e(TAG, "Style parsing failed.");
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
    public void onOkClicked(MarkerDialogFragment dialog) {

    }

    @Override
    public void onCancelClicked(MarkerDialogFragment dialog) {

    }

    @Override
    public void onMarkerIconClicked(MarkerIcon markerIcon) {

    }
}
