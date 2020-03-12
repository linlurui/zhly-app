package com.yizhitong.wisdombuilding.modle;

public class PersonnelDetailModel {
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
        private int id;
        private int pid;//项目id
        private int companyId;// 所属公司id或拜访公司id
        private String empName;//用户名字
        private String idCode;//身份证号

        private String empPhon;//电话号码
        private String empSex;//性别
        private String empNation;//民族
        private String idAddress;//地址
        private String idAgency;//签发机关
        private String idValiddate;//有效期
        private String dateOfBirth;//出生日期
        private String faceUrl;//人脸地址
        private String idphotoScan;//身份证正面
        private String idphotoScan2;//身份证反面
        private String companyName;//所属公司名称或拜访公司名称
        private String floor;//楼层

        private String subordinate;//所属部门或拜访对象
        private String bz;//备注
        private int type;//1,在职人员，2，访客

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

        public int getCompanyId() {
            return companyId;
        }

        public void setCompanyId(int companyId) {
            this.companyId = companyId;
        }

        public String getEmpName() {
            return empName;
        }

        public void setEmpName(String empName) {
            this.empName = empName;
        }

        public String getIdCode() {
            return idCode;
        }

        public void setIdCode(String idCode) {
            this.idCode = idCode;
        }

        public String getEmpPhon() {
            return empPhon;
        }

        public void setEmpPhon(String empPhon) {
            this.empPhon = empPhon;
        }

        public String getEmpSex() {
            return empSex;
        }

        public void setEmpSex(String empSex) {
            this.empSex = empSex;
        }

        public String getEmpNation() {
            return empNation;
        }

        public void setEmpNation(String empNation) {
            this.empNation = empNation;
        }

        public String getIdAddress() {
            return idAddress;
        }

        public void setIdAddress(String idAddress) {
            this.idAddress = idAddress;
        }

        public String getIdAgency() {
            return idAgency;
        }

        public void setIdAgency(String idAgency) {
            this.idAgency = idAgency;
        }

        public String getIdValiddate() {
            return idValiddate;
        }

        public void setIdValiddate(String idValiddate) {
            this.idValiddate = idValiddate;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getFaceUrl() {
            return faceUrl;
        }

        public void setFaceUrl(String faceUrl) {
            this.faceUrl = faceUrl;
        }

        public String getIdphotoScan() {
            return idphotoScan;
        }

        public void setIdphotoScan(String idphotoScan) {
            this.idphotoScan = idphotoScan;
        }

        public String getIdphotoScan2() {
            return idphotoScan2;
        }

        public void setIdphotoScan2(String idphotoScan2) {
            this.idphotoScan2 = idphotoScan2;
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

        public String getSubordinate() {
            return subordinate;
        }

        public void setSubordinate(String subordinate) {
            this.subordinate = subordinate;
        }

        public String getBz() {
            return bz;
        }

        public void setBz(String bz) {
            this.bz = bz;
        }

        public int getType() {
            return type;
        }

        public void setType(int type) {
            this.type = type;
        }
    }

}
