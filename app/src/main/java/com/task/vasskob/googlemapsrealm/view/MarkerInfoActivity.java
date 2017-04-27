package com.task.vasskob.googlemapsrealm.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.realm.DbOperations;
import com.task.vasskob.googlemapsrealm.realm.RealmController;
import com.task.vasskob.googlemapsrealm.view.dialog.adapter.MarkerIconAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.task.vasskob.googlemapsrealm.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.getDefaultMarkerIcons;
import static com.task.vasskob.googlemapsrealm.realm.DbOperations.deleteMarkerInRealm;
import static com.task.vasskob.googlemapsrealm.realm.DbOperations.updateMarkerInRealm;

public class MarkerInfoActivity extends AppCompatActivity implements MarkerInfoView {

    private static final String TAG = MarkerInfoActivity.class.getSimpleName();
    private Marker marker;
    private Dialog dialog;

    @Bind(R.id.marker_label)
    EditText etLabel;

    @Bind(R.id.marker_coordinates)
    TextView tvCoordinates;

    @Bind(R.id.marker_icon)
    ImageButton ibIcon;
    private MarkerIcon clickedMarkerIcon;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showIconChooserDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        updateMarkerInRealm(marker, etLabel.getText().toString(), clickedMarkerIcon);
        finish();
    }

    @OnClick(R.id.btm_delete_marker)
    void onDeleteClick() {
        deleteMarkerInRealm(marker);
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        ButterKnife.bind(this);

        new DbOperations(getApplication());

        Intent intent = getIntent();
        final String markerId = intent.getStringExtra(MapsActivity.MARKER_ID);

        marker = RealmController.with(this).getMarker(Long.parseLong(markerId));
        marker.load();

        etLabel.setText(marker.getTitle());
        tvCoordinates.setText(marker.getLatitude() + " , " + marker.getLongitude());
        MarkerIcon mIcon = marker.getMarkerIcon();
        ibIcon.setImageResource(mIcon.getResId());
        Log.d(TAG, "onCreate" + markerId);
    }

    private void showIconChooserDialog() {
        clickedMarkerIcon=marker.getMarkerIcon();
        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.setContentView(R.layout.icon_list);
        RecyclerView rvMarkerIcons = (RecyclerView) dialog.findViewById(R.id.rvIcons);

        rvMarkerIcons.setHasFixedSize(true);
        rvMarkerIcons.setLayoutManager(new GridLayoutManager(this, COUNT_OF_COLUMN));

        MarkerIconAdapter adapter = new MarkerIconAdapter(getDefaultMarkerIcons(), this);
        adapter.setListener(new MarkerIconAdapter.OnMarkerIconClickListener() {
            @Override
            public void onIconClick(MarkerIcon markerIcon) {
                clickedMarkerIcon = markerIcon;
                ibIcon.setImageResource(markerIcon.getResId());
                dialog.dismiss();
            }
        });
        rvMarkerIcons.setAdapter(adapter);
        dialog.setTitle(R.string.choose_marker_icon);
        dialog.show();
    }

    @Override
    public void showMarkerInfo(Marker marker) {

    }
}
