package com.task.vasskob.googlemaps.screens.common.model.repository;

import com.raizlabs.android.dbflow.config.FlowManager;
import com.raizlabs.android.dbflow.structure.database.DatabaseWrapper;
import com.raizlabs.android.dbflow.structure.database.transaction.FastStoreModelTransaction;
import com.raizlabs.android.dbflow.structure.database.transaction.ITransaction;
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
        //  listener.onSuccess();
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
        add(marker);
    }

    @Override
    public void delete(final Marker marker) {
//        getMarkerById(marker.getId(),
//                new MarkerInfoRetrievedListener() {
//                    @Override
//                    public void onMarkerRetrieved(final MarkerDbFlow marker) {
//                        FlowManager.getDatabase(DbFlowDatabase.class).executeTransaction(new ITransaction() {
//                            @Override
//                            public void execute(DatabaseWrapper databaseWrapper) {
//                                marker.delete();
//                            }
//                        });
//                    }
//                                  }
//        );
        FlowManager.getDatabase(DbFlowDatabase.class).executeTransaction(new ITransaction() {
            @Override
            public void execute(DatabaseWrapper databaseWrapper) {
                new MarkerToMarkerDbFlowMapper().map(marker).delete();
            }
        });
        //listener.onSuccess();
    }

//    private void getMarkerById(String markerId, final MarkerInfoRetrievedListener listener) {
//        SQLite.select()
//                .from(MarkerDbFlow.class)
//                .where(MarkerDbFlow_Table.id.is(markerId))
//                .async()
//                .queryResultCallback(new QueryTransaction.QueryResultCallback<MarkerDbFlow>() {
//                    @Override
//                    public void onQueryResult(QueryTransaction<MarkerDbFlow> transaction, @NonNull CursorResult<MarkerDbFlow> tResult) {
//                        listener.onMarkerRetrieved(tResult.toModelClose());
//                    }
//                }).execute();
//    }

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

//    private interface MarkerInfoRetrievedListener {
//        void onMarkerRetrieved(MarkerDbFlow marker);
//    }
}
