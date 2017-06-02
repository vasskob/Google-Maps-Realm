package com.task.vasskob.googlemaps.screens.detail.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.task.vasskob.googlemaps.Injection;
import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemaps.screens.detail.presenter.MarkerInfoPresenterImpl;
import com.task.vasskob.googlemaps.screens.detail.view.dialog.MarkerIconDialogFragment;
import com.task.vasskob.googlemaps.screens.map.model.Marker;
import com.task.vasskob.googlemaps.screens.map.view.MapsActivity;

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
    private String oldTitle;

    @OnClick(R.id.marker_icon)
    void onIconClick() {
        showMarkerIconDialog();
    }

    @OnClick(R.id.btn_save_marker)
    void onSaveClick() {
        String newTitle = mTitleEditText.getText().toString();
        if (oldTitle.equals(newTitle) && clickedMarkerIcon == null) {
            closeActivity();
        } else {
            marker.setTitle(newTitle);
            marker.setMarkerIcon(clickedMarkerIcon);
            presenter.updateMarkerInDb(marker);
        }
    }

    @OnClick(R.id.btm_delete_marker)
    void onDeleteClick() {
        presenter.deleteMarkerInDb(marker);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_marker_info);
        ButterKnife.bind(this);
        presenter = new MarkerInfoPresenterImpl(Injection.provideMarkerRepository());
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
        MarkerIcon mIcon = marker.getMarkerIcon();
        oldTitle = marker.getTitle();

        mTitleEditText.setText(marker.getTitle());
        mCoordinatesTextView.setText(marker.getLatitude() + " , " + marker.getLongitude());
        mIconImageButton.setImageResource(mIcon.getResId());
    }

    @Override
    public void closeActivity() {
        finish();
    }

    @Override
    public void showError() {
        Toast.makeText(this, R.string.marker_delete_error, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onIconClick(MarkerIcon markerIcon) {

        if (markerIcon != null) {
            clickedMarkerIcon = markerIcon;
            mIconImageButton.setImageResource(clickedMarkerIcon.getResId());
        }
    }
}
