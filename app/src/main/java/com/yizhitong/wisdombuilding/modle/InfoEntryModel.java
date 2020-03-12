package com.yizhitong.wisdombuilding.modle;

import com.blankj.utilcode.util.StringUtils;
import com.yizhitong.wisdombuilding.Constants;
import com.yizhitong.wisdombuilding.utils.MyUtils;


import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class InfoEntryModel {

    private static volatile InfoEntryModel data = new InfoEntryModel();

    private InfoEntryModel() {
    }

    public static InfoEntryModel shared() {
        return data;
    }

    public File idFrontImage = null;
    public File idBackImage = null;
    public File bankImage = null;
    public File faceImage = null;

    public ImageCardInfoModel idFrontInfo = null;
    public ImageCardInfoModel idBackInfo = null;
    public ImageCardInfoModel bankInfo = null;
    public ImageCardInfoModel faceResource = null;
    public ImageCardInfoModel idFrontHeader = null;

    //工人id
    public int workerId;
    //工人头像url
    public String faceHeaderUrl;

    public ProjectChangeModel.DataBean projectData;
    public ConstructionCompanyModel.DataBean buildCompany;
    public DataModel<List<TeamModel>> team;
    public String teams;
    public String buildCompanys;
    public String empCategorys = null;                       //人员类型
    public String jobTypeNames = null;                       //岗位
    public String bankNames = null;                       //银行名称
    public String projectChangeName;
    public String phone = "";                       //手机号
    public String entranceDate = null;
    public InfoEntry.DataBean workType;
    public InfoEntry.DataBean empCategory = null;                       //人员类型
    public InfoEntry.DataBean jobTypeName = null;                       //岗位

    public int projectIdEntry = 0;         //录入时的项目id（集团或者公司登陆时才能切换项目）
    public int constructionId = 0;         // 参建单位ID
    public int workTypenameId = 0;         //所属班组Id
    public String empCategoryTag = null;  //人员类型Tag
    public String jobNameTag = null;      //工种Tag
    public String jobTypenameTag = null;     //岗位Tag

    public int isTeamLeader = 0;             //是否是班组长
    public int isStaff = 0;                 //是否重要人员
    public int isPublicInfo = 0;            //信息是否公开
    public int isContract = 0;             //劳动合同是否签订
    public int isEntrance = 0;               //工人进场承诺书上传状态
    public int isExit = 0;                   //工人退场承诺书上传状态
    public int isWorkConfirm = 0;            ///两制“工作”确认书上传状态
    public int isIDCardPDF = 0;              //身份证正反面文件上传状态
    public int isSafetyTraining = 0;       //安全教育培训是否合格
    public int isContractUpload = 0;          //简易劳动合同上传状态

    public void clearAllData() {
        data = new InfoEntryModel();
        SimpleDateFormat format = new SimpleDateFormat(Constants.DateFormat_10, Locale.getDefault());
        data.entranceDate = format.format(new Date());
    }


    public boolean isIdFrontInfoOK() {
        return (idFrontInfo != null &&
                !StringUtils.isEmpty(idFrontInfo.getData().getTitle()) &&
                !StringUtils.isEmpty(idFrontInfo.getData().getIdNumber()) &&
                !StringUtils.isEmpty(idFrontInfo.getData().getSex()) &&
                !StringUtils.isEmpty(idFrontInfo.getData().getNation()) &&
                !StringUtils.isEmpty(idFrontInfo.getData().getDateOfBirth()) &&
                !StringUtils.isEmpty(idFrontInfo.getData().getAddress()));
    }

    public boolean isIdBackInfoOK() {
        return (idBackInfo != null &&
                !StringUtils.isEmpty(idBackInfo.getData().getIssue()) &&
                !StringUtils.isEmpty(idBackInfo.getData().getIdCardStartdate()));
    }

    public boolean isBankInfoOK() {
        return (bankInfo != null &&
                !StringUtils.isEmpty(bankInfo.getData().getCard_num()));
    }

}
