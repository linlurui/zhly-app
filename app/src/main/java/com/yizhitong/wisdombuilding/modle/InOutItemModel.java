package com.yizhitong.wisdombuilding.modle;

import java.util.List;

public class InOutItemModel {
    private List<PersonnelInOutModel.DataBean> larList;
    private int id;
    private String empName;
    private  String faceUrl;

    public List<PersonnelInOutModel.DataBean> getLarList() {
        return larList;
    }

    public void setLarList(List<PersonnelInOutModel.DataBean> larList) {
        this.larList = larList;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmpName() {
        return empName;
    }

    public void setEmpName(String empName) {
        this.empName = empName;
    }

    public String getFaceUrl() {
        return faceUrl;
    }

    public void setFaceUrl(String faceUrl) {
        this.faceUrl = faceUrl;
    }
}
