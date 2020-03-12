package com.yizhitong.wisdombuilding.modle;

import java.util.List;


public class PersonnelInOutModel {

    private String msg;
    private int code;
    private List<DataBean> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {

        private int id;
        private String empName;//名字
        private String passedTime;//时间
        private String temperature;//温度
        private String sitePhoto;//照片地址
        private String direction;//进出

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

        public String getPassedTime() {
            return passedTime;
        }

        public void setPassedTime(String passedTime) {
            this.passedTime = passedTime;
        }

        public String getTemperature() {
            return temperature;
        }

        public void setTemperature(String temperature) {
            this.temperature = temperature;
        }

        public String getSitePhoto() {
            return sitePhoto;
        }

        public void setSitePhoto(String sitePhoto) {
            this.sitePhoto = sitePhoto;
        }

        public String getDirection() {
            return direction;
        }

        public void setDirection(String direction) {
            this.direction = direction;
        }
    }
}
