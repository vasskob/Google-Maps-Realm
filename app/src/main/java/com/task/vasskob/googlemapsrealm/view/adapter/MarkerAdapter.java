package com.task.vasskob.googlemapsrealm.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.task.vasskob.googlemapsrealm.R;
import com.task.vasskob.googlemapsrealm.model.MarkerIcon;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;

public class MarkerAdapter extends RecyclerView.Adapter<MarkerAdapter.MarkerListHolder> {
    private final LayoutInflater mLayoutInflater;
    private final List<MarkerIcon> markerIconList;

    public MarkerAdapter(List<MarkerIcon> markerIconList, Context context) {
        this.markerIconList = markerIconList;
        mLayoutInflater = LayoutInflater.from(context);
    }

    @Override
    public MarkerListHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mLayoutInflater.inflate(R.layout.icon_item, parent, false);
        return new MarkerListHolder(view);
    }

    @Override
    public void onBindViewHolder(MarkerListHolder holder, int position) {
        holder.imageButton.setImageResource(markerIconList.get(position).getResId());
        holder.imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.setSelected(!v.isSelected());

            }
        });
        Log.d("onBindViewHolder", " markerIcon =" + markerIconList.get(position).getResId());
    }

    @Override
    public int getItemCount() {
        return markerIconList.size();
    }

    static class MarkerListHolder extends RecyclerView.ViewHolder {

        @Bind(R.id.ib_icon_item)
        ImageButton imageButton;

        MarkerListHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }

}
