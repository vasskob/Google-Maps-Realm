package com.task.vasskob.googlemapsrealm.screens.map.view.dialog.adapter;

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

public class MarkerIconAdapter extends RecyclerView.Adapter<MarkerIconAdapter.MarkerListHolder> {
    private final LayoutInflater mLayoutInflater;
    private final List<MarkerIcon> markerIconList;
    private OnMarkerIconClickListener mListener;
    private int mCurrentPosition = -1;

    public MarkerIconAdapter(List<MarkerIcon> markerIconList, Context context) {
        this.markerIconList = markerIconList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setListener(OnMarkerIconClickListener listener) {
        mListener = listener;
    }

    @Override
    public MarkerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.icon_item, parent, false);
        return new MarkerListHolder(view);
    }

    @Override
    public void onBindViewHolder(final MarkerListHolder holder, final int position) {
        holder.imageButton.setImageResource(markerIconList.get(position).getResId());
        holder.imageButton.setSelected(position == mCurrentPosition);
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentPosition = holder.getAdapterPosition();
                mListener.onIconClick(markerIconList.get(mCurrentPosition));
                // TODO: 03/05/17 why need to update all items???
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        if (markerIconList != null) {
            return markerIconList.size();
        } else {
            return 0;
        }
    }

    static class MarkerListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ib_icon_item)
        ImageButton imageButton;

        MarkerListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
        // TODO: 03/05/17 void bind(MarkerIcon marker)
        private void bind(MarkerIcon markerIcon){

        }
    }

    public interface OnMarkerIconClickListener {
        void onIconClick(MarkerIcon markerIcon);
    }

}
