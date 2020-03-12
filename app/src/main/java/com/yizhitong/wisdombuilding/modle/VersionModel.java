package com.yizhitong.wisdombuilding.modle;

public class VersionModel {
    private String msg;
    private int code;
    private DataBean data;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
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
        private String versionName;//版本名
        private int versionNumber;//版本号
        private boolean isMandatoryUpgrade;//是否强制更新
        private String url;//安装包地址
        private String versionContent;//更新内容

        public String getVersionName() {
            return versionName;
        }

        public void setVersionName(String versionName) {
            this.versionName = versionName;
        }

        public int getVersionNumber() {
            return versionNumber;
        }

        public void setVersionNumber(int versionNumber) {
            this.versionNumber = versionNumber;
        }

        public boolean isMandatoryUpgrade() {
            return isMandatoryUpgrade;
        }

        public void setMandatoryUpgrade(boolean mandatoryUpgrade) {
            isMandatoryUpgrade = mandatoryUpgrade;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getVersionContent() {
            return versionContent;
        }

        public void setVersionContent(String versionContent) {
            this.versionContent = versionContent;
        }
    }
}
