package com.task.vasskob.googlemaps.screens.common.model.entity;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.task.vasskob.googlemaps.screens.common.model.db.DbFlowDatabase;

@Table(database = DbFlowDatabase.class)
public class MarkerIcon extends BaseModel {
    @Column
    @PrimaryKey
    private int id;
    @Column
    private int resId;
    @Column
    private boolean isSelected;

    public MarkerIcon(int id, int resId) {
        this.id = id;
        this.resId = resId;
    }

    public boolean isSelected() {
        return isSelected;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public MarkerIcon() {
    }

    public int getResId() {
        return resId;
    }

    public int getId() {
        return id;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public void setId(int id) {
        this.id = id;
    }

}
