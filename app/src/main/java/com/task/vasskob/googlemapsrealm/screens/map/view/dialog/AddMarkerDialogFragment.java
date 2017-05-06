package com.task.vasskob.googlemapsrealm.screens.map.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.common.view.adapter.MarkerIconAdapter;
import com.task.vasskob.googlemapsrealm.screens.common.view.dialog.BaseDialogFragment;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.getMarkerIconsList;

//http://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android

public class AddMarkerDialogFragment extends BaseDialogFragment<AddMarkerDialogFragment.OnDialogClickListener> {

    public static final String TITLE = "title";

    @Bind(R.id.rvIcons)
    RecyclerView markerIconsRecyclerView;

    @Bind(R.id.et_marker_title)
    EditText markerTitleEditText;

    private List<MarkerIcon> mMarkerIconsList;
    private MarkerIconAdapter mMarkerAdapter;


    public interface OnDialogClickListener {
        void onAddClicked(String title, MarkerIcon markerIcon);
    }

    public static AddMarkerDialogFragment newInstance(int title) {

        Bundle args = new Bundle();
        args.putInt(TITLE, title);

        AddMarkerDialogFragment fragment = new AddMarkerDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View dialogView = inflate(this.getActivity(), R.layout.custom_dialog, null);
        ButterKnife.bind(this, dialogView);

        markerIconsRecyclerView.setHasFixedSize(true);
        markerIconsRecyclerView.setLayoutManager(new GridLayoutManager(this.getActivity(), COUNT_OF_COLUMN));

        mMarkerIconsList = getMarkerIconsList();
        mMarkerAdapter = new MarkerIconAdapter(getActivity(), mMarkerIconsList);

        markerIconsRecyclerView.setAdapter(mMarkerAdapter);
        mMarkerAdapter.setSelected(0);

        return new AlertDialog.Builder(getActivity())

                .setView(dialogView)
                .setTitle(getArguments().getInt(TITLE))
                .setCancelable(false)
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                // Positive button clicked
                                getCallback().onAddClicked(getMarkerTitle(), getMarkerIcon());
                            }
                        }
                )
                .setNegativeButton(R.string.cancel,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                dialog.dismiss();
                            }
                        }
                )
                .create();
    }

    public String getMarkerTitle() {
        String title = markerTitleEditText.getText().toString();
        return title.equals("") ? getResources().getString(R.string.default_marker_label) : title;
    }

    public MarkerIcon getMarkerIcon() {
        return mMarkerAdapter.getSelectedMarkerIcon();
    }

}
