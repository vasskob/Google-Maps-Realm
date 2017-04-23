package com.task.vasskob.googlemapsrealm;

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

import com.task.vasskob.googlemapsrealm.model.Marker;
import com.task.vasskob.googlemapsrealm.realm.RealmController;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;

import static com.task.vasskob.googlemapsrealm.MapsActivity.MARKER_ID;


public class MarkerInfo extends AppCompatActivity implements View.OnClickListener {


    private static final String TAG = MarkerInfo.class.getSimpleName();
    private int selectedImageBtn = 0;

    @Bind(R.id.marker_label)
    EditText etLabel;

    @Bind(R.id.marker_coordinates)
    TextView tvCoordinates;

    @Bind(R.id.marker_icon)
    ImageButton ibIcon;
    private Marker marker;
    private Realm realm;
    private Dialog dialog;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showIconChooserDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        updateMarkerInRealm(marker);
    }

    @OnClick(R.id.btm_delete_marker)
    void onDeleteClick() {
        deleteMarkerInRealm(marker);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        ButterKnife.bind(this);
        realm = RealmController.with(this).getRealm();

        Intent intent = getIntent();
        final String markerId = intent.getStringExtra(MARKER_ID);

        marker = RealmController.with(this).getMarker(Long.parseLong(markerId));

        etLabel.setText(marker.getLabel());
        tvCoordinates.setText(marker.getLatitude() + " , " + marker.getLongitude());
        String mIcon = marker.getIcon();
        ibIcon.setImageResource(manageMarkerIcon(mIcon));
        Log.d("MarkerInfo", "onCreate" + markerId);

    }

    private int manageMarkerIcon(String markerIcon) {
        switch (markerIcon) {
            case "ic_icon1":
                return R.drawable.ic_icon1;
            case "ic_icon2":
                return R.drawable.ic_icon2;
            case "ic_icon3":
                return R.drawable.ic_icon3;
            case "ic_icon4":
                return R.drawable.ic_icon4;
            default:
                return R.drawable.ic_default_marker;
        }
    }

    private String manageReverseMarkerIcon(int id) {
        switch (id) {
            case R.id.icon1:
                return "ic_icon1";
            case R.id.icon2:
                return "ic_icon2";
            case R.id.icon3:
                return "ic_icon3";
            case R.id.icon4:
                return "ic_icon4";
            default:
                return "ic_default_marker";
        }
    }

    private void updateMarkerInRealm(final Marker object) {
        realm.beginTransaction();
        object.setLabel(etLabel.getText().toString());
        if (selectedImageBtn != 0) {
            object.setIcon(manageReverseMarkerIcon(selectedImageBtn));
        }
        realm.commitTransaction();
        finish();
    }

    private void deleteMarkerInRealm(final Marker object) {
        realm.beginTransaction();
        object.removeFromRealm();
        realm.commitTransaction();
        finish();
    }

    private void showIconChooserDialog() {

        dialog = new Dialog(this, android.R.style.Theme_DeviceDefault_Dialog);
        dialog.setContentView(R.layout.icon_choser);

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
