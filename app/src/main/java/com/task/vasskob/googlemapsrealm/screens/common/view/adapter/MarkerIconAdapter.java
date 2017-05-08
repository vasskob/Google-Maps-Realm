package com.task.vasskob.googlemapsrealm.screens.common.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.screens.common.model.MarkerIcon;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MarkerIconAdapter extends RecyclerView.Adapter<MarkerIconAdapter.MarkerListHolder> {


    private static final Object SELECTED_STATE_CHANGED = new Object();
    private int mSelectedItemPosition = RecyclerView.NO_POSITION;


    public interface OnMarkerIconClickListener {
        void onMarkerIconClick(View view);
    }

    private final LayoutInflater mLayoutInflater;
    private final List<MarkerIcon> markerIconList;
    private OnMarkerIconClickListener mListener;

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
    public void onBindViewHolder(MarkerListHolder holder, int position, List<Object> payloads) {
        if (payloads.isEmpty()) {
            // full rebind
            onBindViewHolder(holder, position);
        } else {
            MarkerIcon item = markerIconList.get(position);
            if (payloads.contains(SELECTED_STATE_CHANGED)) {
                holder.setSelected(item.isSelected());
            }
        }
    }

    @Override
    public void onBindViewHolder(MarkerListHolder holder, int position) {
        MarkerIcon item = markerIconList.get(position);
        holder.onBind(item);
    }

    @Override
    public int getItemCount() {
        return (markerIconList != null) ? markerIconList.size() : 0;
    }

    public void setSelected(int position) {
        if (mSelectedItemPosition != RecyclerView.NO_POSITION) {
            // deselect previous item
            markerIconList.get(mSelectedItemPosition).setSelected(false);
            notifyItemChanged(mSelectedItemPosition, SELECTED_STATE_CHANGED);
        }
        // select new item
        mSelectedItemPosition = position;
        markerIconList.get(mSelectedItemPosition).setSelected(true);
        notifyItemChanged(mSelectedItemPosition, SELECTED_STATE_CHANGED);
    }


    public MarkerIcon getSelectedMarkerIcon() {
        if (mSelectedItemPosition == RecyclerView.NO_POSITION) {
            return null;
        } else {
            return markerIconList.get(mSelectedItemPosition);
        }
    }

    class MarkerListHolder extends RecyclerView.ViewHolder {
        private final OnMarkerIconClickListener mListener;

        @Bind(R.id.ib_icon_item)
        ImageButton imageButton;

        @OnClick(R.id.ib_icon_item)
        void onMarkerIconClick() {
            MarkerIconAdapter.this.setSelected(getAdapterPosition());
            if (mListener != null) {
                mListener.onMarkerIconClick(itemView);
            }
        }

        MarkerListHolder(View view, OnMarkerIconClickListener listener) {
            super(view);
            ButterKnife.bind(this, view);
            mListener = listener;
        }

        private void onBind(MarkerIcon markerIcon) {
            imageButton.setImageResource(markerIcon.getResId());
            setSelected(markerIcon.isSelected());
        }


        private void setSelected(boolean selected) {
            itemView.setSelected(selected);
        }
    }
}
