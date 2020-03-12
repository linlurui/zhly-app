package com.yizhitong.wisdombuilding.modle;

import com.blankj.utilcode.util.StringUtils;


public class ImageCardInfoModel {

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

        //身份证正面
        private String address; //地址
        private String nation;  //民族
        private String sex; //性别
        private String dateOfBirth; //出生日期
        private String title; //名称
        private String idNumber; //身份证号码
        private String url; //图片路径
        private String empNaticeplace; //身份证人脸头像url
        //身份证反面
        private String issue;
        private String idCardStartdate;
        //银行卡
        private String card_num;

        //人证对比返回对比结果
        private String result;//认证成功true,认证失败false

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getNation() {
            return nation;
        }

        public void setNation(String nation) {
            this.nation = nation;
        }

        public String getSex() {
            return sex;
        }

        public void setSex(String sex) {
            this.sex = sex;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getTitle() {
            return title;
        }

        public void setTitle(String title) {
            this.title = title;
        }

        public String getIdNumber() {
            return idNumber;
        }

        public void setIdNumber(String idNumber) {
            this.idNumber = idNumber;
        }

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getIssue() {
            return issue;
        }

        public void setIssue(String issue) {
            this.issue = issue;
        }

        public String getIdCardStartdate() {
            return idCardStartdate;
        }

        public void setIdCardStartdate(String idCardStartdate) {
            this.idCardStartdate = idCardStartdate;
        }

        public String getResult() {
            return result;
        }

        public void setResult(String result) {
            this.result = result;
        }

        public String getCard_num() {
            return card_num;
        }

        public void setCard_num(String card_num) {
            this.card_num = card_num;
        }

        public String getEmpNaticeplace() {
            return empNaticeplace;
        }

        public void setEmpNaticeplace(String empNaticeplace) {
            this.empNaticeplace = empNaticeplace;
        }

        public boolean setValidDateRange(String range) {
            if (StringUtils.isEmpty(range)) {
                return false;
            }
            String[] parts = range.split("-");
            if (parts.length != 2) {
                return false;
            }
            String start = parts[0].trim();
            String end = parts[1].trim();
            if (start.length() != 8 || end.length() != 8) {
                return false;
            }
            setIdCardStartdate(start);
            return true;
        }
    }
}
