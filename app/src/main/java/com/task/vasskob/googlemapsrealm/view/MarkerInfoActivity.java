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
import android.widget.Toast;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.presenter.MarkerInfoPresenterImpl;
import com.task.vasskob.googlemapsrealm.view.dialog.adapter.MarkerIconAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.task.vasskob.googlemapsrealm.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.getDefaultMarkerIcons;

public class MarkerInfoActivity extends AppCompatActivity implements MarkerInfoView {

    private static final String TAG = MarkerInfoActivity.class.getSimpleName();
    private Marker marker;
    private Dialog dialog;

    @Bind(R.id.marker_label)
    EditText etLabel;

    @Bind(R.id.marker_coordinates)
    TextView tvCoordinates;

    @Bind(R.id.marker_icon)
    ImageButton ibMarkerIcon;
    private MarkerIcon clickedMarkerIcon;
    private MarkerInfoPresenterImpl presenter;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showIconChooserDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        String newTitle = etLabel.getText().toString();
        presenter.updateMarkerInDb(marker, newTitle, clickedMarkerIcon);
        finish();
    }

    @OnClick(R.id.btm_delete_marker)
    void onDeleteClick() {
        presenter.deleteMarkerInDb(marker);
        finish();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        ButterKnife.bind(this);
        presenter = new MarkerInfoPresenterImpl();
    }

    private void showIconChooserDialog() {
        clickedMarkerIcon = marker.getMarkerIcon();
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
                ibMarkerIcon.setImageResource(markerIcon.getResId());
                dialog.dismiss();
            }
        });
        rvMarkerIcons.setAdapter(adapter);
        dialog.setTitle(R.string.choose_marker_icon);
        dialog.show();
    }

    @Override
    protected void onStart() {
        super.onStart();
        presenter.setView(this);
        Intent intent = getIntent();
        final String markerId = intent.getStringExtra(MapsActivity.MARKER_ID);
        presenter.showMarkerInfoById(markerId);
        Log.d(TAG, "onCreate" + markerId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        presenter.clearView();
    }

    @Override
    public void showMarkerInfo(Marker marker) {
        this.marker = marker;
        etLabel.setText(marker.getTitle());
        tvCoordinates.setText(marker.getLatitude() + " , " + marker.getLongitude());
        MarkerIcon mIcon = marker.getMarkerIcon();
        ibMarkerIcon.setImageResource(mIcon.getResId());
    }

    @Override
    public void showErrorToast() {
        Toast.makeText(this, R.string.marker_update_error, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showDeleteToast() {
        Toast.makeText(this, R.string.marker_delete_success, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void showUpdateToast() {
        Toast.makeText(this, R.string.marker_update_success, Toast.LENGTH_SHORT).show();
    }
}
