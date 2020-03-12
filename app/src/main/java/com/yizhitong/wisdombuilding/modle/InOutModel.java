package com.yizhitong.wisdombuilding.modle;

public class InOutModel {
    private int code;
    private String msg;
    private DataBean data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private InOutZZModel zzMap;
        private InOutFKModel fkMap;

        public InOutZZModel getZzMap() {
            return zzMap;
        }

        public void setZzMap(InOutZZModel zzMap) {
            this.zzMap = zzMap;
        }

        public InOutFKModel getFkMap() {
            return fkMap;
        }

        public void setFkMap(InOutFKModel fkMap) {
            this.fkMap = fkMap;
        }
    }
}
