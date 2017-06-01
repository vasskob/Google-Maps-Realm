package com.task.vasskob.googlemaps.screens.common.model.repository;

interface DbFlowSpecification extends Specification {
    void toQueryResults();

    void removeListeners();
}
