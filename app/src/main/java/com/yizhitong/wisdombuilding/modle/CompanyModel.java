package com.yizhitong.wisdombuilding.modle;

import java.util.List;

public class CompanyModel {
    private String msg;
    private int code;
    private List<DataBean> data;

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

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

    public static class DataBean {
        private int id;
        private int pid;
        private String companyName;
        private String floor;
        private String checkIdTime;
        private String personCharge;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public int getPid() {
            return pid;
        }

        public void setPid(int pid) {
            this.pid = pid;
        }

        public String getCompanyName() {
            return companyName;
        }

        public void setCompanyName(String companyName) {
            this.companyName = companyName;
        }

        public String getFloor() {
            return floor;
        }

        public void setFloor(String floor) {
            this.floor = floor;
        }

        public String getCheckIdTime() {
            return checkIdTime;
        }

        public void setCheckIdTime(String checkIdTime) {
            this.checkIdTime = checkIdTime;
        }

        public String getPersonCharge() {
            return personCharge;
        }

        public void setPersonCharge(String personCharge) {
            this.personCharge = personCharge;
        }
    }
}
