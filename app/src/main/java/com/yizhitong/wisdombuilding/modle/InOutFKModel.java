package com.yizhitong.wisdombuilding.modle;

import java.util.List;

public class InOutFKModel {
    private List<InOutItemModel> fkList;
    private int fkSize;

    public List<InOutItemModel> getFkList() {
        return fkList;
    }

    public void setFkList(List<InOutItemModel> fkList) {
        this.fkList = fkList;
    }

    public int getFkSize() {
        return fkSize;
    }

    public void setFkSize(int fkSize) {
        this.fkSize = fkSize;
    }
}
