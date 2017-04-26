package com.task.vasskob.googlemapsrealm.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.view.adapter.MarkerAdapter;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;

//http://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android

public class MarkerDialogFragment extends BaseDialogFragment<MarkerDialogFragment.OnDialogFragmentClickListener> {


    private static final int COUNT_OF_COLUMN = 4;
    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;
    private ArrayList<MarkerIcon> markerIcons;

    public interface OnDialogFragmentClickListener {
        public void onOkClicked(MarkerDialogFragment dialog);

        public void onCancelClicked(MarkerDialogFragment dialog);

        public void onMarkerIconClicked(MarkerIcon markerIcon);
    }


    public static MarkerDialogFragment newInstance(String title) {

        Bundle args = new Bundle();
        args.putString("title", title);

        MarkerDialogFragment fragment = new MarkerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        initMarkers();
        final View dialogView = inflate(this.getActivity(), R.layout.custom_dialog, null);
        ButterKnife.bind(this, dialogView);

        rvMarkerIcons.setHasFixedSize(true);
        rvMarkerIcons.setLayoutManager(new GridLayoutManager(this.getActivity(), COUNT_OF_COLUMN));

        MarkerAdapter adapter = new MarkerAdapter(markerIcons, this.getActivity());
        rvMarkerIcons.setAdapter(adapter);

        int selectedImageBtn = 0;
        return new AlertDialog.Builder(getActivity())

                .setView(dialogView)
                .setTitle(getArguments().getString(getResources().getString(R.string.add_marker)))
                .setCancelable(false)
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Positive button clicked


                                getActivityInstance().onOkClicked(MarkerDialogFragment.this);
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // negative button clicked


                                getActivityInstance().onCancelClicked(MarkerDialogFragment.this);
                            }
                        }
                )
                .create();
    }

    private void initMarkers() {
        markerIcons = new ArrayList<>();
        markerIcons.add(new MarkerIcon(1, R.drawable.ic_icon1));
        markerIcons.add(new MarkerIcon(2, R.drawable.ic_icon2));
        markerIcons.add(new MarkerIcon(3, R.drawable.ic_icon3));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
        markerIcons.add(new MarkerIcon(4, R.drawable.ic_icon4));
    }
}
