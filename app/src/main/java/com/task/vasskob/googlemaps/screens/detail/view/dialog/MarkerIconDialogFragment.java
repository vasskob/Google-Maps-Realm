package com.task.vasskob.googlemaps.screens.detail.view.dialog;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.task.vasskob.googlemaps.R;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerIcon;
import com.task.vasskob.googlemaps.screens.common.view.adapter.MarkerIconAdapter;
import com.task.vasskob.googlemaps.screens.common.view.dialog.BaseDialogFragment;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.view.View.inflate;
import static com.task.vasskob.googlemaps.app.MyApplication.COUNT_OF_COLUMN;
import static com.task.vasskob.googlemaps.app.MyApplication.getMarkerIconsList;

public class MarkerIconDialogFragment extends BaseDialogFragment<MarkerIconDialogFragment.OnDialogClickListener> {

    private static final String TITLE = "marker icon dialog title";

    @BindView(R.id.rvIcons)
    RecyclerView rvMarkerIcons;

    private MarkerIcon clickedMarkerIcon;
    private List<MarkerIcon> mMarkerIconsList;
    private MarkerIconAdapter.OnMarkerIconClickListener mOnMarkerIconClickListener = new MarkerIconAdapter.OnMarkerIconClickListener() {
        @Override
        public void onMarkerIconClick(View view) {
            int position = rvMarkerIcons.getChildAdapterPosition(view);
            if (position != RecyclerView.NO_POSITION) {
                clickedMarkerIcon = mMarkerIconsList.get(position);
            }
        }
    };

    public interface OnDialogClickListener {
        void onIconClick(MarkerIcon markerIcon);
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

        mMarkerIconsList = getMarkerIconsList();
        MarkerIconAdapter adapter = new MarkerIconAdapter(getActivity(), mMarkerIconsList);


        adapter.setListener(mOnMarkerIconClickListener);
        rvMarkerIcons.setAdapter(adapter);

        return new AlertDialog.Builder(getActivity())
                .setView(dialogView)
                .setTitle(getArguments().getInt(TITLE))
                .setCancelable(false)
                .setPositiveButton(R.string.done,
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {
                                getCallback().onIconClick(clickedMarkerIcon);
                            }
                        }
                )
                .create();
    }
}
