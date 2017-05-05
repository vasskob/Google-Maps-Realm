package com.task.vasskob.googlemapsrealm.screens.common.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.listeners.dialog.OnMarkerIconClickListener;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkerIconAdapter extends RecyclerView.Adapter<MarkerIconAdapter.MarkerListHolder> {
    private final LayoutInflater mLayoutInflater;
    private final List<MarkerIcon> markerIconList;
    private OnMarkerIconClickListener mListener;
    private static int previousPosition = -1;

    public MarkerIconAdapter(Context context, List<MarkerIcon> markerIconList) {
        this.markerIconList = markerIconList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setListener(OnMarkerIconClickListener listener) {
        mListener = listener;
    }

    @Override
    public MarkerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.icon_item, parent, false);
        return new MarkerListHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(final MarkerListHolder holder, final int position) {
        holder.onBind(markerIconList);
    }

    @Override
    public int getItemCount() {
        if (markerIconList != null) {
            return markerIconList.size();
        } else {
            return 0;
        }
    }

    class MarkerListHolder extends RecyclerView.ViewHolder {

        private MarkerIcon mMarkerIcon;
        private List<MarkerIcon> mMarkerIconList;
        private final OnMarkerIconClickListener mListener;

        @Bind(R.id.ib_icon_item)
        ImageButton imageButton;

        @OnClick(R.id.ib_icon_item)
        void onMarkerIconClick(View v) {
            mMarkerIcon.setSelected(!mMarkerIcon.isSelected());
            v.setSelected(mMarkerIcon.isSelected());
            if (previousPosition != -1 && previousPosition != getAdapterPosition()) {
                mMarkerIconList.get(previousPosition).setSelected(false);
                notifyItemChanged(previousPosition);
            }
            previousPosition = getAdapterPosition();
            mListener.onIconClick(mMarkerIcon);
        }

        MarkerListHolder(View view, OnMarkerIconClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            mListener = listener;
        }

        private void onBind(List<MarkerIcon> markerIconList) {
            mMarkerIconList = markerIconList;
            mMarkerIcon = markerIconList.get(getAdapterPosition());
            imageButton.setImageResource(mMarkerIcon.getResId());
            imageButton.setSelected(mMarkerIcon.isSelected());
        }
    }
}
