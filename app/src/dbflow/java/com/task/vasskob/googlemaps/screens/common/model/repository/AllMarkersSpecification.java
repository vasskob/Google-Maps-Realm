package com.task.vasskob.googlemaps.screens.common.model.repository;

import android.support.annotation.NonNull;

import com.raizlabs.android.dbflow.sql.language.CursorResult;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;
import com.task.vasskob.googlemaps.listeners.db.OnDataLoadedListener;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerDbFlow;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerDbFlowToMarkerMapper;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class AllMarkersSpecification implements DbFlowSpecification {

    private final OnDataLoadedListener listener;
    private List<Marker> markers = new ArrayList<>();

    public AllMarkersSpecification(OnDataLoadedListener listener) {
        this.listener = listener;
    }

    @Override
    public void toQueryResults() {
        SQLite.select()
                .from(MarkerDbFlow.class)
                .async()
                .queryResultCallback(new QueryTransaction.QueryResultCallback<MarkerDbFlow>() {
                    @Override
                    public void onQueryResult(QueryTransaction<MarkerDbFlow> transaction, @NonNull CursorResult<MarkerDbFlow> tResult) {
                        markers.clear();
                        for (MarkerDbFlow markerDbFlow : tResult.toListClose()) {
                            markers.add(new MarkerDbFlowToMarkerMapper().map(markerDbFlow));
                        }
                        listener.onDataReady(markers);
                    }
                }).execute();
    }

    @Override
    public void removeListeners() {
    }
}
