package com.task.vasskob.googlemaps.screens.common.model.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ProcessModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.Transaction;
import com.task.vasskob.googlemaps.listeners.db.OnMarkerChangeClickListener;
import com.task.vasskob.googlemaps.screens.common.model.db.DbFlowDatabase;
import com.task.vasskob.googlemaps.screens.common.model.entity.MarkerDbFlow;
import com.task.vasskob.googlemaps.screens.common.model.mapper.MarkerToMarkerDbFlowMapper;
import com.task.vasskob.googlemaps.screens.map.model.Marker;

import java.util.ArrayList;
import java.util.List;

public class MarkerRepository implements Repository<Marker> {


    private OnMarkerChangeClickListener listener;
    private DbFlowSpecification mSpecification;
    private List<MarkerDbFlow> markers = new ArrayList<>();

    public MarkerRepository() {

    }

    @Override
    public void add(Marker marker) {
        FastStoreModelTransaction
                .insertBuilder(FlowManager.getModelAdapter(MarkerDbFlow.class))
                .add(new MarkerToMarkerDbFlowMapper().map(marker))
                .build()
                .execute(FlowManager.getWritableDatabase(DbFlowDatabase.class));
    }

    @Override
    public void add(final List<Marker> list) {
        markers.clear();
        for (Marker marker : list) {
            markers.add(new MarkerToMarkerDbFlowMapper().map(marker));
        }

        FastStoreModelTransaction
                .saveBuilder(FlowManager.getModelAdapter(MarkerDbFlow.class))
                .addAll(markers)
                .build()
                .execute(FlowManager.getWritableDatabase(DbFlowDatabase.class));
    }

    @Override
    public void update(Marker marker) {
        MarkerDbFlow markerDbFlow = new MarkerToMarkerDbFlowMapper().map(marker);

        FlowManager.getDatabase(DbFlowDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<MarkerDbFlow>() {
                            @Override
                            public void processModel(MarkerDbFlow markerDbFlow, DatabaseWrapper wrapper) {
                                markerDbFlow.save();
                            }
                        }).add(markerDbFlow).build())  // add elements (can also handle multiple)
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        listener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        listener.onSuccess();
                    }
                }).build().execute();
    }

    @Override
    public void delete(final Marker marker) {
        MarkerDbFlow markerDbFlow = new MarkerToMarkerDbFlowMapper().map(marker);
        FlowManager.getDatabase(DbFlowDatabase.class)
                .beginTransactionAsync(new ProcessModelTransaction.Builder<>(
                        new ProcessModelTransaction.ProcessModel<MarkerDbFlow>() {
                            @Override
                            public void processModel(MarkerDbFlow markerDbFlow, DatabaseWrapper wrapper) {
                                markerDbFlow.delete();
                            }
                        }).add(markerDbFlow).build())
                .error(new Transaction.Error() {
                    @Override
                    public void onError(Transaction transaction, Throwable error) {
                        listener.onError();
                    }
                })
                .success(new Transaction.Success() {
                    @Override
                    public void onSuccess(Transaction transaction) {
                        listener.onSuccess();
                    }
                }).build().execute();
    }

    @Override
    public List<Marker> query(Specification specification) {
        mSpecification = (DbFlowSpecification) specification;
        mSpecification.toQueryResults();
        // return data via callback in Specification
        return null;
    }

    @Override
    public void setResultListener(OnMarkerChangeClickListener listener) {
        this.listener = listener;
    }

    @Override
    public void removeListener() {
        if (mSpecification != null) {
            mSpecification.removeListeners();
        }
    }
}
