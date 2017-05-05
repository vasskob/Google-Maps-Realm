package com.task.vasskob.googlemapsrealm.screens.marker_details.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.listeners.dialog.OnMarkerIconClickListener;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.common.view.dialog.BaseDialogFragment;
import com.task.vasskob.googlemapsrealm.screens.common.view.adapter.MarkerIconAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.getMarkerIconsList;

public class MarkerIconDialogFragment extends BaseDialogFragment<MarkerIconDialogFragment.OnDialogClickListener>{

    private static final String TITLE = "marker icon dialog title";

    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    private MarkerIcon clickedMarkerIcon;


    public interface OnDialogClickListener {
        void onIconClick(MarkerIconDialogFragment dialog);
    }

    public static MarkerIconDialogFragment newInstance(int title) {
        Bundle args = new Bundle();
        args.putInt(TITLE, title);
        MarkerIconDialogFragment fragment = new MarkerIconDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = inflate(this.getActivity(), R.layout.icon_list, null);
        ButterKnife.bind(this, dialogView);

        rvMarkerIcons.setHasFixedSize(true);
        rvMarkerIcons.setLayoutManager(new GridLayoutManager(getActivity(), COUNT_OF_COLUMN));

        MarkerIconAdapter adapter = new MarkerIconAdapter(getActivity(), getMarkerIconsList());
        adapter.setListener(new OnMarkerIconClickListener() {
            @Override
            public void onIconClick(MarkerIcon markerIcon) {
                clickedMarkerIcon = markerIcon;
            }
        });
        rvMarkerIcons.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(getArguments().getInt(TITLE))
                .setCancelable(false)
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                              getCallback().onIconClick(MarkerIconDialogFragment.this);
                            }
                        }
                )
                .create();
    }

    public MarkerIcon getSelectedMarkerIcon() {
        return clickedMarkerIcon;
    }
}
