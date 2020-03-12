package com.yizhitong.wisdombuilding.modle;

import com.raizlabs.android.dbflow.annotation.Column;
import com.raizlabs.android.dbflow.annotation.PrimaryKey;
import com.raizlabs.android.dbflow.annotation.Table;
import com.raizlabs.android.dbflow.structure.BaseModel;

@Table(database = AppDatabase.class)
public class JurisdictionModel extends BaseModel {
    @PrimaryKey(autoincrement = false)
    private int id;
    @Column
    private String url;
    @Column
    private String privilegesName;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrivilegesName() {
        return privilegesName;
    }

    public void setPrivilegesName(String privilegesName) {
        this.privilegesName = privilegesName;
    }
}
