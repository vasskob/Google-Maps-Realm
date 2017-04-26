package com.task.vasskob.googlemapsrealm.view;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.realm.DbOperations;
import com.task.vasskob.googlemapsrealm.realm.RealmController;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.task.vasskob.googlemapsrealm.realm.DbOperations.deleteMarkerInRealm;
import static com.task.vasskob.googlemapsrealm.realm.DbOperations.updateMarkerInRealm;
import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.manageMarkerIcon;
import static com.task.vasskob.googlemapsrealm.util.ManageMarkerIcon.manageReverseMarkerIcon;


public class MarkerInfoActivity extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = MarkerInfoActivity.class.getSimpleName();
    private int selectedImageBtn = 0;

    @Bind(R.id.marker_label)
    EditText etLabel;

    @Bind(R.id.marker_coordinates)
    TextView tvCoordinates;

    @Bind(R.id.marker_icon)
    ImageButton ibIcon;
    private Marker marker;
    private Dialog dialog;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showIconChooserDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        updateMarkerInRealm(marker, etLabel.getText().toString(), selectedImageBtn);
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

        etLabel.setText(marker.getLabel());
        tvCoordinates.setText(marker.getLatitude() + " , " + marker.getLongitude());
        MarkerIcon mIcon = marker.getMarkerIcon();
        ibIcon.setImageResource(mIcon.getResId());
        Log.d(TAG, "onCreate" + markerId);
    }

    private void showIconChooserDialog() {

        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.setContentView(R.layout.icon_chooser);

        ImageButton icon1 = (ImageButton) dialog.findViewById(R.id.icon1);
        icon1.setOnClickListener(this);
        ImageButton icon2 = (ImageButton) dialog.findViewById(R.id.icon2);
        icon2.setOnClickListener(this);
        ImageButton icon3 = (ImageButton) dialog.findViewById(R.id.icon3);
        icon3.setOnClickListener(this);
        ImageButton icon4 = (ImageButton) dialog.findViewById(R.id.icon4);
        icon4.setOnClickListener(this);
        dialog.setTitle(R.string.choose_icon);

        dialog.show();
    }

    @Override
    public void onClick(View v) {
        selectedImageBtn = v.getId();
        dialog.dismiss();
        ibIcon.setImageResource(manageMarkerIcon(manageReverseMarkerIcon(selectedImageBtn)));
    }
}
