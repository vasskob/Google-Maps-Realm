package com.task.vasskob.googlemapsrealm.screens.map.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;
import com.task.vasskob.googlemapsrealm.screens.map.view.dialog.adapter.MarkerIconAdapter;

import butterknife.Bind;
import butterknife.ButterKnife;

import static android.view.View.inflate;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemapsrealm.app.MyApplication.getMarkerIconsList;

//http://stackoverflow.com/questions/23408756/create-a-general-class-for-custom-dialog-in-java-android

public class AddMarkerDialogFragment extends BaseDialogFragment<AddMarkerDialogFragment.OnDialogClickListener> {

    public static final String TITLE = "title";

    @Bind(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    @Bind(R.id.et_marker_title)
    EditText etMarkerTitle;

    private MarkerIcon defaultMarkerIcon = new MarkerIcon(5, R.drawable.ic_default_marker);
    private MarkerIcon clickedMarkerIcon;
    private String mTitle;


    public interface OnDialogClickListener {
        void onAddClicked(AddMarkerDialogFragment dialog);
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

        rvMarkerIcons.setHasFixedSize(true);
        rvMarkerIcons.setLayoutManager(new GridLayoutManager(this.getActivity(), COUNT_OF_COLUMN));

        MarkerIconAdapter adapter = new MarkerIconAdapter(getMarkerIconsList(), this.getActivity());
        adapter.setListener(new MarkerIconAdapter.OnMarkerIconClickListener() {
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
                                // Positive button clicked
                                mTitle = etMarkerTitle.getText().toString();
                                if (clickedMarkerIcon != null)
                                    Log.d("OnPositiveBtnClick", "Title = " + mTitle + " icon = " + clickedMarkerIcon.getId());

                                getCallback().onAddClicked(AddMarkerDialogFragment.this);
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
        return mTitle.equals("") ? getResources().getString(R.string.default_marker_label) : mTitle;
    }

    public MarkerIcon getMarkerIcon() {
        return clickedMarkerIcon == null ? defaultMarkerIcon : clickedMarkerIcon;
    }

}
