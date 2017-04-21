package com.task.vasskob.googlemapsrealm;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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


public class MarkerInfo extends AppCompatActivity {


    @Bind(R.id.marker_label)
    EditText etLabel;

    @Bind(R.id.marker_coordinates)
    TextView tvCoordinates;

    @Bind(R.id.marker_icon)
    ImageButton ibIcon;
    private Marker marker;
    private Realm realm;

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        marker.setLabel(etLabel.getText().toString());
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

    private void updateMarkerInRealm(final Marker object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                realm.copyToRealmOrUpdate(object);
            }
        });

    }

    private void deleteMarkerInRealm(final Marker object) {
        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                Marker result = realm.where(Marker.class).equalTo("id", object.getId()).findFirst();
                result.removeFromRealm();
            }
        });
        finish();
    }
}
