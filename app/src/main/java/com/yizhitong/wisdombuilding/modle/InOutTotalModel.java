package com.yizhitong.wisdombuilding.modle;

public class InOutTotalModel {
    private String msg;
    private int code;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private ZZModel zz;
        private FKModel fk;

        public ZZModel getZz() {
            return zz;
        }

        public void setZz(ZZModel zz) {
            this.zz = zz;
        }

        public FKModel getFk() {
            return fk;
        }

        public void setFk(FKModel fk) {
            this.fk = fk;
        }
    }
}
