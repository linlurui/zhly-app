package com.yizhitong.wisdombuilding.api;


import com.yizhitong.wisdombuilding.modle.ApiResp;
import com.yizhitong.wisdombuilding.modle.CompanyModel;
import com.yizhitong.wisdombuilding.modle.DataModel;
import com.yizhitong.wisdombuilding.modle.ImageCardInfoModel;
import com.yizhitong.wisdombuilding.modle.InOutModel;
import com.yizhitong.wisdombuilding.modle.InOutTotalModel;
import com.yizhitong.wisdombuilding.modle.LoginUserModel;
import com.yizhitong.wisdombuilding.modle.PersonnelDetailModel;
import com.yizhitong.wisdombuilding.modle.PersonnelInOutModel;
import com.yizhitong.wisdombuilding.modle.PersonnelListModel;
import com.yizhitong.wisdombuilding.modle.PrivilegesModel;
import com.yizhitong.wisdombuilding.modle.VersionModel;

import java.util.Map;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {
    String api = "api";

    Call<ApiResp<VersionModel>> getVersion();

    Call<ApiResp<PrivilegesModel>> getSystemPrivileges(int id, int i);


    //用户登录
    @FormUrlEncoded
    @POST(api + "/system/computer/login")
    Call<ApiResp<LoginUserModel>> loginUser(@Field(value = "userAccount") String userAccount,
                                            @Field(value = "userPassword") String userPassword,
                                            @Field(value = "entry") int entry);


    //修改密码
    @FormUrlEncoded
    @POST(api + "/system/application/updateUserPassword")
    Call<ApiResp<DataModel>> changePassword(@Field(value = "id") int id,
                                                      @Field(value = "userPassword") String userPassword,
                                                      @Field(value = "newPassword") String newPassword,
                                                      @Field(value = "confirmPassword") String confirmPassword);

    //人员进出统计
    @FormUrlEncoded
    @POST(api + "/lyPersonnel/getPersonnelDT")
    Call<ApiResp<InOutTotalModel>> getPersonnelDT(@Field(value = "pid") int pid,
                                                  @Field(value = "time") String time);

    //人员进出列表
    @FormUrlEncoded
    @POST(api + "/lyPersonnel/getPersonnelRecord")
    Call<ApiResp<InOutModel>> getPersonnelRecord(@Field(value = "pid") int pid,
                                                 @Field(value = "time") String time);

    //人员详情
    @FormUrlEncoded
    @POST(api + "/lyPersonnel/selectPersonnelById")
    Call<ApiResp<PersonnelDetailModel>> selectPersonnelById(@Field(value = "personnelId") int personnelId);


    //身份证/银行卡识别
    @Multipart
    @POST(api + "/projectWorkersApi/getAliOcrIdCard")
    Call<ApiResp<ImageCardInfoModel>> uploadCardImage(@Part MultipartBody.Part file,
                                                      @Query(value = "configStr") String folderName);

    //人证对比
    @Multipart
    @POST(api + "/projectWorkersApi/queryWitnessComparison")
    Call<ApiResp<ImageCardInfoModel>> queryWitnessComparison(@Part MultipartBody.Part file,
                                                             @Query(value = "url") String url);

    //人员录入信息提交
    @FormUrlEncoded
    @POST(api + "/lyPersonnel/insertPersonnel")
    Call<ApiResp<DataModel>> insertPersonnel(@FieldMap Map<String, Object> map);

    //查询公司列表
    @FormUrlEncoded
    @POST(api + "/lyCompany/selectList")
    Call<ApiResp<CompanyModel>> selectList(@Field(value = "pid") int pid);


    //查询人员列表
    @FormUrlEncoded
    @POST(api + "/lyPersonnel/selectPersonnelCompany")
    Call<ApiResp<PersonnelListModel>> selectPersonnelCompany(@Field(value = "pid") int pid,
                                                             @Field(value = "empName") String empName);

    //查询个人进出记录
    @FormUrlEncoded
    @POST(api + "/lyRecord/selectPersonnelRecord")
    Call<ApiResp<PersonnelInOutModel>> selectPersonnelRecord(@Field(value = "employeeId") int pid,
                                                             @Field(value = "passedTime") String passedTime);
}
