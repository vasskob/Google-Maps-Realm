package com.task.vasskob.googlemapsrealm.screens.marker_details.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.model.Marker;
import com.task.vasskob.googlemapsrealm.screens.map.view.MapsActivity;
import com.task.vasskob.googlemapsrealm.screens.marker_details.presenter.MarkerInfoPresenterImpl;
import com.task.vasskob.googlemapsrealm.screens.marker_details.view.dialog.MarkerIconDialogFragment;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkerInfoActivity extends AppCompatActivity implements MarkerInfoView, MarkerIconDialogFragment.OnDialogClickListener {

    private static final String TAG = MarkerInfoActivity.class.getSimpleName();
    private static final String DIALOG_FRAGMENT_TAG = "marker icon dialog";
    private Marker marker;

    @Bind(R.id.marker_label)
    EditText mTitleEditText;

    @Bind(R.id.marker_coordinates)
    TextView mCoordinatesTextView;

    @Bind(R.id.marker_icon)
    ImageButton mIconImageButton;

    private MarkerIcon clickedMarkerIcon;
    private MarkerInfoPresenterImpl presenter;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showMarkerIconDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        String newTitle = mTitleEditText.getText().toString();
        presenter.updateMarkerInDb(marker, newTitle, clickedMarkerIcon);
        // TODO: 03/05/17 check for result and then finish activity. what if error?
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

    private void showMarkerIconDialog() {
        MarkerIconDialogFragment dialogFragment =
                MarkerIconDialogFragment.newInstance(R.string.choose_marker_icon);
        dialogFragment.show(getSupportFragmentManager(), DIALOG_FRAGMENT_TAG);
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
        mTitleEditText.setText(marker.getTitle());
        mCoordinatesTextView.setText(marker.getLatitude() + " , " + marker.getLongitude());
        MarkerIcon mIcon = marker.getMarkerIcon();
        mIconImageButton.setImageResource(mIcon.getResId());
    }

    @Override
    public void onIconClick(MarkerIconDialogFragment dialog) {
        clickedMarkerIcon = dialog.getSelectedMarkerIcon();
        if (clickedMarkerIcon != null) {
            mIconImageButton.setImageResource(clickedMarkerIcon.getResId());
        }
    }
}
